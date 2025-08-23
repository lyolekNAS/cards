package org.sav.fornas.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.sav.fornas.dto.cards.TrainedWordDto;
import org.sav.fornas.dto.cards.WordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.sav.fornas.dto.google.TranslationResponse.Translation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WordServiceTest {

	private RestTemplate jwtRestTemplate;
	private RestTemplate gTranslateRestTemplate;
	private WordService service;

	@BeforeEach
	void setUp() {
		jwtRestTemplate = mock(RestTemplate.class);
		gTranslateRestTemplate = mock(RestTemplate.class);
		service = new WordService(jwtRestTemplate, gTranslateRestTemplate);
	}

	@Test
	void getWordsByUser_shouldCallJwtRestTemplate() {
		List<WordDto> expected = List.of(new WordDto());
		when(jwtRestTemplate.exchange(eq("/word/user/all"), eq(HttpMethod.GET),
				isNull(), any(ParameterizedTypeReference.class)))
				.thenReturn(new org.springframework.http.ResponseEntity<>(expected, org.springframework.http.HttpStatus.OK));

		List<WordDto> result = service.getWordsByUser();

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void saveWord_shouldPostWordDto() {
		WordDto input = new WordDto();
		WordDto output = new WordDto();
		when(jwtRestTemplate.postForObject("/word/save", input, WordDto.class)).thenReturn(output);

		WordDto result = service.saveWord(input);

		assertThat(result).isEqualTo(output);
	}

	@Test
	void deleteWord_shouldCallDelete() {
		service.deleteWord(123L);
		verify(jwtRestTemplate).delete("/word/delete?id=123");
	}

	@Test
	void findWord_existing_shouldReturnFromService() {
		WordDto dto = new WordDto();
		when(jwtRestTemplate.getForObject("/word/find?w=cat", WordDto.class)).thenReturn(dto);

		WordDto result = service.findWord("cat");

		assertThat(result).isEqualTo(dto);
	}

	@Test
	void findWord_missing_shouldTranslate() {
		when(jwtRestTemplate.getForObject("/word/find?w=dog", WordDto.class)).thenReturn(null);

		TranslationResponse response = new TranslationResponse();
		Translation translation = new Translation();
		translation.setTranslatedText("собака");
		response.setData(new TranslationResponse.Data());
		response.getData().setTranslations(List.of(translation));
		when(gTranslateRestTemplate.postForObject(anyString(), isNull(), eq(TranslationResponse.class)))
				.thenReturn(response);

		WordDto result = service.findWord("dog");

		assertThat(result.getEnglish()).isEqualTo("dog");
		assertThat(result.getUkrainian()).isEqualTo("собака");
	}

	@Test
	void getWord_shouldCallRestTemplate() {
		WordDto dto = new WordDto();
		when(jwtRestTemplate.getForObject("/word/train", WordDto.class)).thenReturn(dto);

		WordDto result = service.getWord();

		assertThat(result).isEqualTo(dto);
	}

	@Test
	void setTrained_shouldPostToJwtRestTemplate() {
		TrainedWordDto dto = new TrainedWordDto();
		service.setTrained(dto);
		verify(jwtRestTemplate).postForObject("/word/trained", dto, String.class);
	}
}
