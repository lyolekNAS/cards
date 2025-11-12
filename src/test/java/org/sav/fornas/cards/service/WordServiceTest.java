package org.sav.fornas.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
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
	private WordControllerApi wordControllerApi;
	private StateLimitControllerApi stateLimitControllerApi;
	private WordService service;

	@BeforeEach
	void setUp() {
		jwtRestTemplate = mock(RestTemplate.class);
		gTranslateRestTemplate = mock(RestTemplate.class);
		wordControllerApi = mock(WordControllerApi.class);
		stateLimitControllerApi = mock(StateLimitControllerApi.class);

		service = new WordService(jwtRestTemplate, gTranslateRestTemplate, wordControllerApi, stateLimitControllerApi);
	}

	@Test
	void getWordsByUser_shouldCallJwtRestTemplate() {
		List<WordDto> expected = List.of(new WordDto());
		when(wordControllerApi.getAllByUser())
				.thenReturn(expected);

		List<WordDto> result = service.getWordsByUser();

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void saveWord_shouldPostWordDto() {
		WordDto input = new WordDto();
		WordDto output = new WordDto();
		when(wordControllerApi.addWord(input)).thenReturn(output);

		WordDto result = service.saveWord(input);

		assertThat(result).isEqualTo(output);
	}

	@Test
	void deleteWord_shouldCallDelete() {
		service.deleteWord(123L);
		verify(wordControllerApi).deleteWord(123L);
	}

	@Test
	void findWord_existing_shouldReturnFromService() {
		WordDto dto = new WordDto();
		when(wordControllerApi.findWord("cat")).thenReturn(dto);

		WordDto result = service.findWord("cat");

		assertThat(result).isEqualTo(dto);
	}

	@Test
	void findWord_missing_shouldTranslate() {
		when(wordControllerApi.findWord("dog")).thenReturn(null);

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
		when(wordControllerApi.findWordToTrain()).thenReturn(dto);
		when(jwtRestTemplate.getForObject("/word/train", WordDto.class)).thenReturn(dto);

		WordDto result = service.getWord();

		assertThat(result).isEqualTo(dto);
	}

	@Test
	void setTrained_shouldPostToJwtRestTemplate() {
		TrainedWordDto dto = new TrainedWordDto();
		service.setTrained(dto);
		verify(wordControllerApi).processTrainedWord(dto);
	}
}
