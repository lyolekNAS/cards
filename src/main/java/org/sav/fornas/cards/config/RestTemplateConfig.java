package org.sav.fornas.cards.config;

import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.property.GoogleProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

	@Value("${app-props.url.cards-back}")
	private String cardsApiBaseUrl;

	@Bean
	public RestTemplate jwtRestTemplate(RestTemplateBuilder builder, OAuth2AuthorizedClientManager clientManager) {
		return builder
				.rootUri(cardsApiBaseUrl)
				.additionalInterceptors((request, body, execution) -> {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					if (auth != null && auth.getPrincipal() instanceof OidcUser) {

						log.debug(">>> inside if");
						String clientRegistrationId = "cards";

						log.debug(">>> inside if 2");
						OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
								.withClientRegistrationId(clientRegistrationId)
								.principal(auth)
								.build();

						log.debug(">>> inside if 3");
						log.debug(">>> authorizeRequest:{}", authorizeRequest);
						OAuth2AuthorizedClient authorizedClient = clientManager.authorize(authorizeRequest);

						log.debug(">>> inside if 4");
						if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
							log.debug(">>> inside if 5");
							String token = authorizedClient.getAccessToken().getTokenValue();
							log.debug(">>> inside if 6");
							request.getHeaders().setBearerAuth(token);
							log.debug(">>> Adding refreshed Bearer token: {}", token);
						} else {
							log.info(">>> Could not obtain access token");
						}
					}

					log.debug(">>> before return");
					return execution.execute(request, body);
				})
				.build();
	}

	@Bean
	public RestTemplate gTranslateRestTemplate(RestTemplateBuilder builder, GoogleProperties googleProperties) {
		log.trace(">>> gTranslateURL={}, key={}", googleProperties.getTranslateUrl(), googleProperties.getApiKey().substring(0, 7));
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
	public RestTemplate restTemplateCommon(RestTemplateBuilder builder) {
		return builder.build();
	}
}
