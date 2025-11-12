package org.sav.fornas.cards.config;

import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.property.GoogleProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigGPTTest {

	private final SecurityConfig config = new SecurityConfig();
	private final RestTemplateBuilder builder = new RestTemplateBuilder();

	@Test
	void oidcUserService_shouldMapRolesToAuthorities() {
		OidcIdToken idToken = new OidcIdToken("token-value", Instant.now(), Instant.now().plusSeconds(3600), Map.of("sub", "123"));

		OidcUser oidcUser = mock(OidcUser.class);
		when(oidcUser.getClaims()).thenReturn(Map.of("roles", List.of("ROLE_CARDS_USER")));
		when(oidcUser.getIdToken()).thenReturn(idToken);
		when(oidcUser.getUserInfo()).thenReturn(null);

		OidcUserService delegate = mock(OidcUserService.class);
		when(delegate.loadUser(any())).thenReturn(oidcUser);

		OAuth2UserService<OidcUserRequest, OidcUser> service = config.oidcUserService(delegate);
		OidcUser result = service.loadUser(mock(OidcUserRequest.class));

		assertThat(result.getAuthorities())
				.extracting("authority")
				.contains("ROLE_CARDS_USER");
	}


	@Test
	void authorizedClientManager_shouldReturnNonNull() {
		var repo = mock(ClientRegistrationRepository.class);
		var authRepo = mock(OAuth2AuthorizedClientRepository.class);

		OAuth2AuthorizedClientManager manager = config.authorizedClientManager(repo, authRepo);

		assertThat(manager).isNotNull();
	}

	@Test
	void jwtRestTemplate_shouldContainInterceptor() {
		OAuth2AuthorizedClientManager mockManager = mock(OAuth2AuthorizedClientManager.class);
		RestTemplate restTemplate = config.jwtRestTemplate(builder, mockManager);

		assertThat(restTemplate.getInterceptors()).hasSize(1);
	}

	@Test
	void gTranslateRestTemplate_shouldAddApiKey() throws Exception {
		GoogleProperties props = new GoogleProperties();
		props.setTranslateUrl("https://fake");
		props.setApiKey("test-key");

		RestTemplate rt = config.gTranslateRestTemplate(builder, props);

		assertThat(rt.getInterceptors()).hasSize(1);
	}

	@Test
	void forwardedHeaderFilter_shouldBeHighestPrecedence() {
		FilterRegistrationBean<ForwardedHeaderFilter> bean = config.forwardedHeaderFilter();

		assertThat(bean.getOrder()).isEqualTo(Integer.MIN_VALUE); // Ordered.HIGHEST_PRECEDENCE
		assertThat(bean.getFilter()).isInstanceOf(ForwardedHeaderFilter.class);
	}
}

