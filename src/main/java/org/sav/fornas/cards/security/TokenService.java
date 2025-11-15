package org.sav.fornas.cards.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final OAuth2AuthorizedClientManager clientManager;

	public String getAccessToken() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}

		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId("cards")
				.principal(auth)
				.build();

		OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);
		if (client == null || client.getAccessToken() == null) {
			return null;
		}

		return client.getAccessToken().getTokenValue();
	}
}

