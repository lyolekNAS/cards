package org.sav.fornas.cards.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpController {
	private final RestTemplate jwtRestTemplate;

	@GetMapping("/get")
	public String get(@RequestParam("role") String role) {
		return jwtRestTemplate.getForObject("http://cards-back:8452/api/" + role + "/me", String.class);
	}

}