package org.sav.fornas.cards.config;

import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.property.GoogleProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Value("${app-props.url.cards-back}")
	private String cardsApiBaseUrl;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, OidcUserService delegate) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/error", "/public/**", "/css/**", "/js/**").permitAll()
						.anyRequest().hasRole("CARDS_USER")
				)
//				.redirectToHttps(Customizer.withDefaults())
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfo -> userInfo
								.oidcUserService(oidcUserService(delegate))
						)
				)
				.logout(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public OidcUserService oidcUserServiceDelegate() {
		return new OidcUserService();
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(OidcUserService delegate) {
		return userRequest -> {
			OidcUser oidcUser = delegate.loadUser(userRequest);

			Collection<? extends GrantedAuthority> mappedAuthorities =
					((List<String>) oidcUser.getClaims().getOrDefault("roles", List.of()))
							.stream()
							.map(SimpleGrantedAuthority::new)
							.toList();

			return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
		};
	}


	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository) {

		OAuth2AuthorizedClientProvider authorizedClientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
						.authorizationCode()
						.refreshToken() // <-- додає можливість оновлювати токен
						.build();

		DefaultOAuth2AuthorizedClientManager manager =
				new DefaultOAuth2AuthorizedClientManager(
						clientRegistrationRepository,
						authorizedClientRepository
				);
		manager.setAuthorizedClientProvider(authorizedClientProvider);
		return manager;
	}

	@Bean
	public RestTemplate jwtRestTemplate(RestTemplateBuilder builder, OAuth2AuthorizedClientManager clientManager) {
		return builder
				.rootUri(cardsApiBaseUrl)
				.additionalInterceptors((request, body, execution) -> {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					if (auth != null && auth.getPrincipal() instanceof OidcUser) {

						String clientRegistrationId = "cards";

						OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
								.withClientRegistrationId(clientRegistrationId)
								.principal(auth)
								.build();

						OAuth2AuthorizedClient authorizedClient = clientManager.authorize(authorizeRequest);

						if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
							String token = authorizedClient.getAccessToken().getTokenValue();
							request.getHeaders().setBearerAuth(token);
							log.debug(">>> Adding refreshed Bearer token: {}", token);
						} else {
							log.info(">>> Could not obtain access token");
						}
					}
					return execution.execute(request, body);
				})
				.build();
	}

	@Bean
	public RestTemplate gTranslateRestTemplate(RestTemplateBuilder builder, GoogleProperties googleProperties) {
		log.debug(">>> gTranslateURL={}, key={}", googleProperties.getTranslateUrl(), googleProperties.getApiKey());
		return builder
				.rootUri(googleProperties.getTranslateUrl())
				.additionalInterceptors((request, body, execution) -> {
					// Додаємо заголовок X-goog-api-key
					request.getHeaders().add("X-goog-api-key", googleProperties.getApiKey());
					return execution.execute(request, body);
				})
				.build();
	}







	@Bean
	public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
		FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
		filterRegBean.setFilter(new ForwardedHeaderFilter());
		filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegBean;
	}
}
