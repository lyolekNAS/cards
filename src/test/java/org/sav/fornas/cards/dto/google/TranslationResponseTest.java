package org.sav.fornas.cards.dto.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationResponseTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void shouldDeserializeGoogleTranslateResponse() throws Exception {
		String json = """
				{
				  "data": {
				    "translations": [
				      { "translatedText": "Привіт" }
				    ]
				  }
				}
				""";

		TranslationResponse response = objectMapper.readValue(json, TranslationResponse.class);

		assertThat(response.getData()).isNotNull();
		assertThat(response.getData().getTranslations()).hasSize(1);
		assertThat(response.getData().getTranslations().get(0).getTranslatedText()).isEqualTo("Привіт");
	}

	@Test
	void gettersAndSettersShouldWorkForNestedStructure() {
		TranslationResponse.Translation translation = new TranslationResponse.Translation();
		translation.setTranslatedText("Hello");

		TranslationResponse.Data data = new TranslationResponse.Data();
		data.setTranslations(List.of(translation));

		TranslationResponse response = new TranslationResponse();
		response.setData(data);

		assertThat(response.getData()).isSameAs(data);
		assertThat(response.getData().getTranslations()).containsExactly(translation);
		assertThat(response.getData().getTranslations().get(0).getTranslatedText()).isEqualTo("Hello");
	}
}
