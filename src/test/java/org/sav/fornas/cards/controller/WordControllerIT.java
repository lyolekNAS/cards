package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sav.fornas.cards.service.WordService;
import org.sav.fornas.dto.cards.WordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(WordController.class)
class WordControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private WordService wordService;

	@Test
	void testShowForm_returnsWordForm() throws Exception {
		WordDto word = new WordDto();
		word.setEnglish("chair");

		Mockito.when(wordService.findWord("chair")).thenReturn(word);

		mockMvc.perform(get("/edit").param("w", "chair"))
				.andExpect(status().isOk())
				.andExpect(view().name("word-form"))
				.andExpect(model().attributeExists("word"))
				.andExpect(model().attribute("word", word));
	}
}

