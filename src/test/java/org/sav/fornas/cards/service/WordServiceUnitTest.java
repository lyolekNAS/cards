package org.sav.fornas.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.sav.fornas.cards.client.cardsback.model.StatisticDto;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.client.cardsback.model.WordsPageDtoWordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WordServiceUnitTest {

	private RestTemplate gTranslateRestTemplate;
	@Mock
	private WordControllerApi wordControllerApi;
	private DictionaryControllerApi dictionaryControllerApi;
	private StateLimitControllerApi stateLimitControllerApi;
	@InjectMocks
	private WordService wordService;

	@BeforeEach
	void setUp() {
		gTranslateRestTemplate = mock(RestTemplate.class);
		wordControllerApi = mock(WordControllerApi.class);
		dictionaryControllerApi = mock(DictionaryControllerApi.class);
		stateLimitControllerApi = mock(StateLimitControllerApi.class);

		wordService = new WordService(gTranslateRestTemplate, wordControllerApi, dictionaryControllerApi, stateLimitControllerApi);
	}

	@Test
	void testGetWordsByUser() {
		// given
		WordsPageDtoWordDto mockPage = new WordsPageDtoWordDto();
		WordDto w1 = new WordDto();
		WordDto w2 = new WordDto();
		mockPage.setContent(List.of(w1, w2));

		when(wordControllerApi.getAllByUser(0, 10, ""))
				.thenReturn(mockPage);

		// when
		WordsPageDtoWordDto result = wordService.getWordsByUser(0, 10, null);

		// then
		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(wordControllerApi).getAllByUser(0, 10, "");
	}


	@Test
	void testSaveWord() {

		WordDto input = new WordDto();
		input.setEnglish("chair");

		WordDto saved = new WordDto();
		saved.setEnglish("chair");

		when(wordControllerApi.addWord(input))
				.thenReturn(saved);

		WordDto result = wordService.saveWord(input);
		assertNotNull(result);
		assertEquals("chair", result.getEnglish());
		verify(wordControllerApi).addWord(input);
	}

	@Test
	void testDeleteWord() {

		Long id = 1L;
		wordService.deleteWord(id);
		verify(wordControllerApi).deleteWord(1L);
	}

	@Test
	void testFindWord_existing() {

		WordDto word = new WordDto();
		word.setEnglish("table");
		when(wordControllerApi.findWord("table")).thenReturn(word);

		WordDto result = wordService.findWord("table");

		assertEquals("table", result.getEnglish());
	}

	@Test
	void testFindWord_notFound_callsTranslation() {
		when(wordControllerApi.findWord("sofa"))
				.thenReturn(null);

		TranslationResponse translationResponse = new TranslationResponse();
		TranslationResponse.Translation translation = new TranslationResponse.Translation();
		TranslationResponse.Data data = new TranslationResponse.Data();
		data.setTranslations(List.of(translation));
		translation.setTranslatedText("диван");
		translationResponse.setData(data);

		when(gTranslateRestTemplate.postForObject(
						Mockito.contains("/v2?target=uk&source=en&q=sofa"),
						Mockito.isNull(),
						eq(TranslationResponse.class)))
				.thenReturn(translationResponse);

		WordDto result = wordService.findWord("sofa");

		assertEquals("sofa", result.getEnglish());
		assertEquals("диван", result.getUkrainian());

		verify(wordControllerApi).findWord("sofa");
		verify(gTranslateRestTemplate).postForObject(anyString(), any(), eq(TranslationResponse.class));
	}

	@Test
	void testGetWord() {

		WordDto word = new WordDto();
		word.setEnglish("desk");
		when(wordControllerApi.findWordToTrain()).thenReturn(word);

		WordDto result = wordService.getWord();

		assertEquals("desk", result.getEnglish());
	}

	@Test
	void testSetTrained() {

		TrainedWordDto trained = new TrainedWordDto();
		trained.setId(1L);

		wordService.setTrained(trained);

		Mockito.verify(wordControllerApi).processTrainedWord(trained);
	}

	@Test
	void getStatistics_ReturnsStatisticDto() {
		StatisticDto expectedStatistic = new StatisticDto();
		when(wordControllerApi.getStatistic())
				.thenReturn(expectedStatistic);

		StatisticDto result = wordService.getStatistics();

		assertEquals(expectedStatistic, result);
	}
}
