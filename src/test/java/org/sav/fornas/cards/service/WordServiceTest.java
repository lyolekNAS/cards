package org.sav.fornas.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.sav.fornas.cards.client.cardsback.model.StatisticDto;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.client.cardsback.model.WordsPageDtoWordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.sav.fornas.dto.google.TranslationResponse.Translation;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WordServiceTest {

	private RestTemplate gTranslateRestTemplate;
	private WordControllerApi wordControllerApi;
	private DictionaryControllerApi dictionaryControllerApi;
	private StateLimitControllerApi stateLimitControllerApi;
	private WordService service;

	@BeforeEach
	void setUp() {
		gTranslateRestTemplate = mock(RestTemplate.class);
		wordControllerApi = mock(WordControllerApi.class);
		dictionaryControllerApi = mock(DictionaryControllerApi.class);
		stateLimitControllerApi = mock(StateLimitControllerApi.class);

		service = new WordService(gTranslateRestTemplate, wordControllerApi, dictionaryControllerApi, stateLimitControllerApi);
	}

	@Test
	void getWordsByUser_shouldCallJwtRestTemplate() {
		WordsPageDtoWordDto expected = new WordsPageDtoWordDto();

		when(wordControllerApi.getAllByUser(eq(0), eq(10), eq("")))
				.thenReturn(expected);

		WordsPageDtoWordDto result = service.getWordsByUser(0, 10, null);

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

		WordDto result = service.getWord();

		assertThat(result).isEqualTo(dto);
	}

	@Test
	void setTrained_shouldPostToJwtRestTemplate() {
		TrainedWordDto dto = new TrainedWordDto();
		service.setTrained(dto);
		verify(wordControllerApi).processTrainedWord(dto);
	}

	@Test
	void getStatistics_shouldCallApi(){
		service.getStatistics();
		verify(wordControllerApi).getStatistic();
	}

	@Test
	void getStatistics_shouldReturn(){
		StatisticDto statDto = new StatisticDto();
		when(wordControllerApi.getStatistic()).thenReturn(statDto);
		StatisticDto result = service.getStatistics();
		assertThat(result).isEqualTo(statDto);
	}
}
