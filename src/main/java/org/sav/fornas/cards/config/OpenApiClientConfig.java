package org.sav.fornas.cards.config;

import lombok.RequiredArgsConstructor;
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.api.MeControllerApi;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class OpenApiClientConfig {

	private final RestTemplate jwtRestTemplate;

	@Value("${app-props.url.cards-back}")
	private String cardsApiBaseUrl;


	@Bean
	public ApiClient apiClient() {
		ApiClient client = new ApiClient(jwtRestTemplate);
		client.setBasePath(cardsApiBaseUrl);
		return client;
	}

	@Bean
	public MeControllerApi meControllerApi(ApiClient apiClient) {
		return new MeControllerApi(apiClient);
	}

	@Bean
	public WordControllerApi wordControllerApi(ApiClient apiClient) {
		return new WordControllerApi(apiClient);
	}

	@Bean
	public StateLimitControllerApi stateLimitControllerApi(ApiClient apiClient) {
		return new StateLimitControllerApi(apiClient);
	}
}