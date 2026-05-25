package org.sav.fornas.cards.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void getAccessToken_shouldReturnNullWhenAuthenticationIsMissing() {
		SecurityContextHolder.clearContext();
		StubAuthorizedClientManager manager = new StubAuthorizedClientManager();
		TokenService service = new TokenService(manager);

		String token = service.getAccessToken();

		assertThat(token).isNull();
		assertThat(manager.calls).isZero();
	}

	@Test
	void getAccessToken_shouldReturnNullWhenManagerReturnsNullClient() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
		SecurityContextHolder.getContext().setAuthentication(auth);

		StubAuthorizedClientManager manager = new StubAuthorizedClientManager();
		manager.clientToReturn = null;
		TokenService service = new TokenService(manager);

		String token = service.getAccessToken();

		assertThat(token).isNull();
		assertThat(manager.calls).isEqualTo(1);
		assertThat(manager.lastRequest).isNotNull();
		assertThat(manager.lastRequest.getClientRegistrationId()).isEqualTo("cards");
		assertThat(manager.lastRequest.getPrincipal()).isSameAs(auth);
	}

	@Test
	void getAccessToken_shouldReturnTokenValueWhenAuthorizedClientHasToken() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
		SecurityContextHolder.getContext().setAuthentication(auth);

		StubAuthorizedClientManager manager = new StubAuthorizedClientManager();
		manager.clientToReturn = authorizedClientWithToken("token-123");
		TokenService service = new TokenService(manager);

		String token = service.getAccessToken();

		assertThat(token).isEqualTo("token-123");
		assertThat(manager.calls).isEqualTo(1);
	}

	@Test
	void getAccessToken_shouldReturnNullWhenAuthorizedClientAccessTokenIsNull() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
		SecurityContextHolder.getContext().setAuthentication(auth);

		StubAuthorizedClientManager manager = new StubAuthorizedClientManager();
		manager.clientToReturn = authorizedClientWithNullAccessToken();
		TokenService service = new TokenService(manager);

		String token = service.getAccessToken();

		assertThat(token).isNull();
		assertThat(manager.calls).isEqualTo(1);
	}

	private static OAuth2AuthorizedClient authorizedClientWithToken(String tokenValue) {
		OAuth2AccessToken token = new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER,
				tokenValue,
				Instant.now().minusSeconds(60),
				Instant.now().plusSeconds(3600)
		);
		return new OAuth2AuthorizedClient(clientRegistration(), "user", token);
	}

	private static OAuth2AuthorizedClient authorizedClientWithNullAccessToken() {
		return new OAuth2AuthorizedClient(clientRegistration(), "user", new OAuth2AccessToken(
				OAuth2AccessToken.TokenType.BEARER,
				"placeholder",
				Instant.now().minusSeconds(60),
				Instant.now().plusSeconds(3600)
		)) {
			@Override
			public OAuth2AccessToken getAccessToken() {
				return null;
			}
		};
	}

	private static ClientRegistration clientRegistration() {
		return ClientRegistration.withRegistrationId("cards")
				.clientId("cards-client")
				.clientSecret("cards-secret")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
				.authorizationUri("https://example.org/oauth2/authorize")
				.tokenUri("https://example.org/oauth2/token")
				.build();
	}

	private static class StubAuthorizedClientManager implements OAuth2AuthorizedClientManager {
		private OAuth2AuthorizedClient clientToReturn;
		private OAuth2AuthorizeRequest lastRequest;
		private int calls;

		@Override
		public OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest) {
			calls++;
			lastRequest = authorizeRequest;
			return clientToReturn;
		}
	}
}
