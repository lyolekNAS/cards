package org.sav.fornas.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sav.fornas.dto.cards.TrainedWordDto;
import org.sav.fornas.dto.cards.WordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WordServiceUnitTest {


	RestTemplate jwtRestTemplate;
	RestTemplate gTranslateRestTemplate;
	WordService wordService;

	@BeforeEach
	void setUp() {
		jwtRestTemplate = mock(RestTemplate.class);
		gTranslateRestTemplate = mock(RestTemplate.class);
		wordService = new WordService(jwtRestTemplate, gTranslateRestTemplate);
	}

	@Test
	void testGetWordsByUser() {

		List<WordDto> mockWords = List.of(new WordDto(), new WordDto());

		when(jwtRestTemplate.exchange(
						eq("/word/user/all"),
						eq(HttpMethod.GET),
						Mockito.isNull(),
						Mockito.<ParameterizedTypeReference<List<WordDto>>>any()))
				.thenReturn(new ResponseEntity<>(mockWords, HttpStatus.OK));

		List<WordDto> result = wordService.getWordsByUser();

		assertNotNull(result);
		assertEquals(2, result.size());

		verify(jwtRestTemplate).exchange(
				eq("/word/user/all"),
				eq(HttpMethod.GET),
				Mockito.isNull(),
				Mockito.<ParameterizedTypeReference<List<WordDto>>>any());
	}

	@Test
	void testSaveWord() {

		WordDto input = new WordDto();
		input.setEnglish("chair");

		WordDto saved = new WordDto();
		saved.setEnglish("chair");

		when(jwtRestTemplate.postForObject("/word/save", input, WordDto.class))
				.thenReturn(saved);

		WordDto result = wordService.saveWord(input);
		assertNotNull(result);
		assertEquals("chair", result.getEnglish());
		verify(jwtRestTemplate).postForObject("/word/save", input, WordDto.class);
	}

	@Test
	void testDeleteWord() {

		Long id = 1L;
		wordService.deleteWord(id);
		verify(jwtRestTemplate).delete("/word/delete?id=1");
	}

	@Test
	void testFindWord_existing() {

		WordDto word = new WordDto();
		word.setEnglish("table");
		when(jwtRestTemplate.getForObject("/word/find?w=table", WordDto.class))
				.thenReturn(word);

		WordDto result = wordService.findWord("table");

		assertEquals("table", result.getEnglish());
	}

	@Test
	void testFindWord_notFound_callsTranslation() {
		when(jwtRestTemplate.getForObject("/word/find?w=sofa", WordDto.class))
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

		verify(jwtRestTemplate).getForObject(anyString(), eq(WordDto.class));
		verify(gTranslateRestTemplate).postForObject(anyString(), any(), eq(TranslationResponse.class));
	}

	@Test
	void testGetWord() {

		WordDto word = new WordDto();
		word.setEnglish("desk");
		when(jwtRestTemplate.getForObject(
						contains("/word/train"),
						eq(WordDto.class)
		)).thenReturn(word);

		WordDto result = wordService.getWord();

		assertEquals("desk", result.getEnglish());
	}

	@Test
	void testSetTrained() {

		TrainedWordDto trained = new TrainedWordDto();
		trained.setId(1L);

		wordService.setTrained(trained);

		Mockito.verify(jwtRestTemplate).postForObject("/word/trained", trained, String.class);
	}
}
