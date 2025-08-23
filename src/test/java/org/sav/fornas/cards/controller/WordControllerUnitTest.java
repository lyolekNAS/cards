package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sav.fornas.cards.service.WordService;
import org.sav.fornas.dto.cards.TrainedWordDto;
import org.sav.fornas.dto.cards.WordDto;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class WordControllerUnitTest {

	@Mock
	private WordService wordService;

	@InjectMocks
	private WordController wordController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(wordController).build();
	}

	@Test
	void testViewInfo() throws Exception {
		List<WordDto> words = List.of(new WordDto(), new WordDto());
		Mockito.when(wordService.getWordsByUser()).thenReturn(words);

		mockMvc.perform(get("/words"))
				.andExpect(status().isOk())
				.andExpect(view().name("words"))
				.andExpect(model().attributeExists("words"))
				.andExpect(model().attribute("words", words));
	}

	@Test
	void testShowForm_withWord() throws Exception {
		WordDto word = new WordDto();
		word.setEnglish("chair");
		Mockito.when(wordService.findWord("chair")).thenReturn(word);

		mockMvc.perform(get("/edit").param("w", "chair"))
				.andExpect(status().isOk())
				.andExpect(view().name("word-form"))
				.andExpect(model().attributeExists("word"))
				.andExpect(model().attribute("word", word));
	}

	@Test
	void testShowForm_withoutWord() throws Exception {
		WordDto word = new WordDto();
		Mockito.when(wordService.findWord("")).thenReturn(word);

		mockMvc.perform(get("/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("word-form"))
				.andExpect(model().attributeExists("word"));
	}

	@Test
	void testAddWord() throws Exception {
		mockMvc.perform(get("/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("add-word"));
	}

	@Test
	void testSaveWord() throws Exception {
		WordDto savedWord = new WordDto();
		savedWord.setEnglish("desk");

		Mockito.when(wordService.saveWord(Mockito.any(WordDto.class))).thenReturn(savedWord);

		mockMvc.perform(post("/save")
						.param("english", "desk")
						.param("ukrainian", "стіл"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/edit?w=desk"));
	}

	@Test
	void testDeleteWord() throws Exception {
		mockMvc.perform(get("/delete").param("id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/words"));

		Mockito.verify(wordService).deleteWord(1L);
	}

	@Test
	void testTrain() throws Exception {
		WordDto word = new WordDto();
		Mockito.when(wordService.getWord()).thenReturn(word);

		mockMvc.perform(get("/train"))
				.andExpect(status().isOk())
				.andExpect(view().name("train"))
				.andExpect(model().attribute("word", word));
	}

	@Test
	void testPublicTrain() throws Exception {
		mockMvc.perform(get("/public/train"))
				.andExpect(status().isOk())
				.andExpect(view().name("train"))
				.andExpect(model().attributeExists("word"));
	}

	@Test
	void testSetTrained() throws Exception {
		TrainedWordDto trained = new TrainedWordDto();
		trained.setId(1L);

		mockMvc.perform(get("/trained")
						.param("wordId", "1")
						.param("trainedTimes", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/train"));

		Mockito.verify(wordService).setTrained(Mockito.any(TrainedWordDto.class));
	}
}

