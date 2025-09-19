package org.sav.fornas.cards.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {

	private final OAuth2AuthorizedClientService authorizedClientService;



	@RequestMapping("/me")
	public Authentication me(@AuthenticationPrincipal OidcUser oidcUser) {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@RequestMapping("/oidc")
	public OidcUser oidc(@AuthenticationPrincipal OidcUser user) {
		return user;
	}

	@RequestMapping("/roles")
	public Authentication roles() {
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		log.info("User:{}", user);
		return user;
	}
}
