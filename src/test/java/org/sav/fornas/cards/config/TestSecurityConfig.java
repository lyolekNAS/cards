package org.sav.fornas.cards.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;

@TestConfiguration
public class TestSecurityConfig {

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
			ClientRegistrationRepository repo) {
		return new InMemoryOAuth2AuthorizedClientService(repo);
	}
}