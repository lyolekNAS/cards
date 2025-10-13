package org.sav.fornas.cards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.service.WordService;
import org.sav.fornas.dto.cards.*;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WordControllerUnitTest {

	private WordService wordService;
	private WordController controller;
	private Model model;

	@BeforeEach
	void setUp() {
		wordService = mock(WordService.class);
		controller = new WordController(wordService);
		model = mock(Model.class);
	}

	@Test
	void viewInfo_shouldAddWordsToModel() {
		List<WordDto> mockWords = List.of(new WordDto());
		when(wordService.getWordsByUser()).thenReturn(mockWords);

		String view = controller.viewInfo(model);

		verify(model).addAttribute("words", mockWords);
		assertThat(view).isEqualTo("words");
	}

	@Test
	void showForm_shouldReturnWordForm() {
		WordDto dto = new WordDto();
		when(wordService.findWord("dog")).thenReturn(dto);

		String view = controller.showForm(model, "dog");

		verify(model).addAttribute("word", dto);
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void saveWord_shouldRedirectToEdit() {
		WordDto saved = new WordDto();
		saved.setEnglish("table");
		when(wordService.saveWord(any())).thenReturn(saved);

		String view = controller.saveWord(saved);

		verify(wordService).saveWord(saved);
		assertThat(view).isEqualTo("redirect:/edit?w=table");
	}

	@Test
	void deleteWord_shouldRedirectToWords() {
		String view = controller.deleteWord(1L);

		verify(wordService).deleteWord(1L);
		assertThat(view).isEqualTo("redirect:/words");
	}

//	@Test
//	void train_shouldAddWordAndReturnTrainView() {
//		WordDto dto = new WordDto();
//		when(wordService.getWord()).thenReturn(dto);
//
//		String view = controller.train(model);
//
//		verify(model).addAttribute("word", dto);
//		assertThat(view).isEqualTo("train");
//	}

	@Test
	void train_shouldAddAttributesAndReturnTrainView() {
		// given
		WordDto word = new WordDto();
		word.setState(WordStateDto.STAGE_1);
		word.setEnglishCnt(2);
		word.setUkrainianCnt(3);

		StateLimitDto stateLimit = new StateLimitDto();
		stateLimit.setAttempt(5);
		stateLimit.setColor("green");

		when(wordService.getWord()).thenReturn(word);
		when(wordService.getStateLimit(WordStateDto.STAGE_1.getId())).thenReturn(stateLimit);

		// when
		String view = controller.train(model);

		// then
		verify(model).addAttribute("word", word);
		verify(model).addAttribute("stateColor", "green");
		verify(model).addAttribute(eq("progressPercent"), anyDouble());
		assertThat(view).isEqualTo("train");
	}

	@Test
	void setTrained_shouldCallServiceAndRedirect() {
		TrainedWordDto dto = new TrainedWordDto();

		String view = controller.setTrained(dto);

		verify(wordService).setTrained(dto);
		assertThat(view).isEqualTo("redirect:/train");
	}

	@Test
	void statistic_ReturnsStatisticView() {
		StatisticDto statisticDto = new StatisticDto();
		when(wordService.getStatistics()).thenReturn(statisticDto);

		String result = controller.statistic(model);

		assertEquals("statistic", result);
		verify(model).addAttribute("statistics", statisticDto);
	}
}
