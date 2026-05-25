package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.service.DictionaryService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DictionaryControllerIntTest {

	private StubDictionaryService dictionaryService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		dictionaryService = new StubDictionaryService();
		DictionaryController dictionaryController = new DictionaryController(dictionaryService);
		mockMvc = MockMvcBuilders.standaloneSetup(dictionaryController).build();
	}

	@Test
	void getNewWords_shouldReturnWordsAsJson() throws Exception {
		dictionaryService.wordsToReturn = List.of(
				new WordDto().id(1L).english("cat"),
				new WordDto().id(2L).english("dog")
		);

		mockMvc.perform(get("/getNewWords"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].english").value("cat"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].english").value("dog"));

		assertThat(dictionaryService.getNewWordsCalls).isEqualTo(1);
	}

	@Test
	void enrichWithExamples_shouldReturnWordAsJson() throws Exception {
		dictionaryService.wordToReturn = new WordDto()
				.id(5L)
				.english("table")
				.examples(List.of("Put the book on the table."));

		mockMvc.perform(get("/enrichWithExamples").param("word", "table"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.english").value("table"))
				.andExpect(jsonPath("$.examples[0]").value("Put the book on the table."));

		assertThat(dictionaryService.enrichWithExamplesCalls).isEqualTo(1);
		assertThat(dictionaryService.lastWordArg).isEqualTo("table");
	}

	@Test
	void enrichWithExamples_withoutWordParam_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/enrichWithExamples"))
				.andExpect(status().isBadRequest());

		assertThat(dictionaryService.getNewWordsCalls).isZero();
		assertThat(dictionaryService.enrichWithExamplesCalls).isZero();
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
