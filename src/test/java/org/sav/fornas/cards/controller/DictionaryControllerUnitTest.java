package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.service.DictionaryService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryControllerUnitTest {

	private StubDictionaryService dictionaryService;
	private DictionaryController controller;

	@BeforeEach
	void setUp() {
		dictionaryService = new StubDictionaryService();
		controller = new DictionaryController(dictionaryService);
	}

	@Test
	void getNewWords_shouldReturnWordsFromService() {
		dictionaryService.wordsToReturn = List.of(
				new WordDto().id(1L).english("cat"),
				new WordDto().id(2L).english("dog")
		);

		List<WordDto> result = controller.getNewWords();

		assertThat(dictionaryService.getNewWordsCalls).isEqualTo(1);
		assertThat(result).isEqualTo(dictionaryService.wordsToReturn);
	}

	@Test
	void enrichWithExamples_shouldReturnWordFromService() {
		dictionaryService.wordToReturn = new WordDto()
				.id(10L)
				.english("table")
				.examples(List.of("A table is in the room."));

		WordDto result = controller.enrichWithExamples("table");

		assertThat(dictionaryService.enrichWithExamplesCalls).isEqualTo(1);
		assertThat(dictionaryService.lastWordArg).isEqualTo("table");
		assertThat(result).isEqualTo(dictionaryService.wordToReturn);
	}

	private static class StubDictionaryService extends DictionaryService {
		private List<WordDto> wordsToReturn = List.of();
		private WordDto wordToReturn;
		private int getNewWordsCalls;
		private int enrichWithExamplesCalls;
		private String lastWordArg;

		private StubDictionaryService() {
			super(null, null);
		}

		@Override
		public List<WordDto> getNewWords() {
			getNewWordsCalls++;
			return wordsToReturn;
		}

		@Override
		public WordDto enrichWithExamples(String w) {
			enrichWithExamplesCalls++;
			lastWordArg = w;
			return wordToReturn;
		}
	}
}
