package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sav.fornas.cards.client.cardsback.model.StateLimitDto;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.client.cardsback.model.WordsPageDtoWordDto;
import org.sav.fornas.cards.service.WordService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class WordControllerIntTest {

	@Mock
	private WordService wordService;

	@InjectMocks
	private WordController wordController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(wordController)
				.setViewResolvers((viewName, locale) -> {
					if (viewName.startsWith("redirect:")) {
						return new org.springframework.web.servlet.view.RedirectView(viewName.substring("redirect:".length()));
					}
					if (viewName.startsWith("forward:")) {
						return new org.springframework.web.servlet.view.InternalResourceView(viewName.substring("forward:".length()));
					}
					return new org.springframework.web.servlet.view.InternalResourceView(viewName);
				})
				.build();

	}

	@Test
	void testViewInfo() throws Exception {
		WordsPageDtoWordDto dto = new WordsPageDtoWordDto();
		when(wordService.getWordsByUser(0, 20, "")).thenReturn(dto);

		mockMvc.perform(get("/words")
						.param("page", "0")
						.param("size", "20")
						.param("state", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("words"))
				.andExpect(model().attributeExists("words"))
				.andExpect(model().attribute("words", dto));
	}

	@Test
	void testShowForm_withWord() throws Exception {
		String engWord = "chair";
		WordDto word = new WordDto();
		word.setEnglish(engWord);
		when(wordService.findWord(engWord)).thenReturn(word);

		mockMvc.perform(get("/edit").param("w", engWord))
				.andExpect(status().isOk())
				.andExpect(view().name("word-form"))
				.andExpect(model().attributeExists("word"))
				.andExpect(model().attribute("word", word));
	}

	@Test
	void testShowForm_withoutWord() throws Exception {
		WordDto word = new WordDto();
		when(wordService.findWord("")).thenReturn(word);

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
		savedWord.setUkrainian("стіл");

		when(wordService.saveWord(savedWord)).thenReturn(savedWord);

		mockMvc.perform(post("/save")
						.param("english", savedWord.getEnglish())
						.param("ukrainian", savedWord.getUkrainian()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/edit?w=" + savedWord.getEnglish()));
	}

	@Test
	void testDeleteWord() throws Exception {
		mockMvc.perform(get("/delete").param("id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/words"));

		verify(wordService).deleteWord(1L);
	}

//	@Test
//	void testTrain() throws Exception {
//		WordDto word = new WordDto();
//		when(wordService.getWord()).thenReturn(word);
//
//		mockMvc.perform(get("/train"))
//				.andExpect(status().isOk())
//				.andExpect(view().name("train"))
//				.andExpect(model().attribute("word", word));
//	}

	@Test
	void train_shouldReturnTrainViewWithModelAttributes() throws Exception {
		// given
		WordDto word = new WordDto();
		word.setState(WordDto.StateEnum.STAGE_1);
		word.setEnglishCnt(2);
		word.setUkrainianCnt(3);

		StateLimitDto stateLimit = new StateLimitDto();
		stateLimit.setAttempt(5);
		stateLimit.setColor("green");

		when(wordService.getWord()).thenReturn(word);
		when(wordService.getStateLimit(WordDto.StateEnum.STAGE_1.getValue())).thenReturn(stateLimit);

		// when & then
		mockMvc.perform(get("/train"))
				.andExpect(status().isOk())
				.andExpect(view().name("train"))
				.andExpect(model().attribute("word", word))
				.andExpect(model().attributeExists("progressPercent"))
				.andExpect(model().attribute("stateColor", "green"));
	}


	@Test
	void testSetTrained() throws Exception {
		TrainedWordDto word = new TrainedWordDto();
		word.setId(1L);
		word.setLang(TrainedWordDto.LangEnum.EN);
		word.setSuccess(true);

		mockMvc.perform(get("/trained")
						.param("id", String.valueOf(word.getId()))
						.param("success", String.valueOf(word.getSuccess()))
						.param("lang", word.getLang().name()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/train"));

		ArgumentCaptor<TrainedWordDto> captor = ArgumentCaptor.forClass(TrainedWordDto.class);
		verify(wordService).setTrained(captor.capture());

		TrainedWordDto captured = captor.getValue();
		assertEquals(word, captured);
	}
}

