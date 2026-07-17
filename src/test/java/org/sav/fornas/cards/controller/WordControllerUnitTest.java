package org.sav.fornas.cards.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.model.*;
import org.sav.fornas.cards.security.TokenService;
import org.sav.fornas.cards.service.DictionaryService;
import org.sav.fornas.cards.service.WordService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WordControllerUnitTest {

	private WordService wordService;
	private DictionaryService dictionaryService;
	private TokenService tokenService;
	private WordController controller;
	private Model model;

	@BeforeEach
	void setUp() {
		wordService = mock(WordService.class);
		dictionaryService = mock(DictionaryService.class);
		tokenService = mock(TokenService.class);
		controller = new WordController(wordService, dictionaryService, tokenService);
		model = mock(Model.class);
	}

	@Test
	void viewInfo_shouldAddWordsToModel() {
		WordsPageDtoWordDto mockPage = new WordsPageDtoWordDto();
		when(wordService.getWordsByUser(0, 20, "")).thenReturn(mockPage);

		String view = controller.viewInfo(0, 20, "", model);

		verify(model).addAttribute("words", mockPage);
		assertThat(view).isEqualTo("words");
	}

	@Test
	void showForm_shouldReturnWordFormAndSetDefaultState() {
		WordDto dto = new WordDto();
		when(wordService.findWord("dog")).thenReturn(dto);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getServletPath()).thenReturn("/edit");

		String view = controller.showForm(model, "dog", request);

		verify(model).addAttribute("word", dto);
		verify(model).addAttribute("returnUrl", "/edit?w=dog");
		assertThat(dto.getState()).isEqualTo(WordDto.StateEnum.STAGE_1);
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void showCardForm_shouldReturnWordFormAndSetDefaultState() {
		WordDto dto = new WordDto();
		when(wordService.findCardWord("cat")).thenReturn(dto);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getServletPath()).thenReturn("/edit-card");

		String view = controller.showCardForm(model, "cat", request);

		verify(model).addAttribute("word", dto);
		verify(model).addAttribute("returnUrl", "/edit?w=cat");
		assertThat(dto.getState()).isEqualTo(WordDto.StateEnum.STAGE_1);
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void saveWord_shouldRedirectToGivenReturnUrl() {
		WordDto saved = new WordDto();
		saved.setEnglish("table");
		when(wordService.saveWord(any())).thenReturn(saved);

		String view = controller.saveWord(saved, "/edit?w=table");

		verify(wordService).saveWord(saved);
		assertThat(view).isEqualTo("redirect:/edit?w=table");
	}

	@Test
	void resetWord_shouldResetAndRedirectToEdit() {
		String view = controller.resetWord(7L, "dog");

		verify(dictionaryService).resetWord(7L);
		assertThat(view).isEqualTo("redirect:/edit?w=dog");
	}

	@Test
	void setSkipped_shouldCallServiceAndRedirect() {
		String view = controller.setSkipped(11L, "/words");

		verify(wordService).setMark(11L, "SKIP");
		assertThat(view).isEqualTo("redirect:/words");
	}

	@Test
	void setKnown_shouldCallServiceAndRedirect() {
		String view = controller.setKnown(12L, "/words");

		verify(wordService).setMark(12L, "KNOWN");
		assertThat(view).isEqualTo("redirect:/words");
	}

	@Test
	void deleteWord_shouldRedirectToWords() {
		String view = controller.deleteWord(1L);

		verify(wordService).deleteWord(1L);
		assertThat(view).isEqualTo("redirect:/words");
	}

	@Test
	void pick5Paused_shouldCallServiceAndRedirect() {
		when(wordService.pick5Paused()).thenReturn(5);

		String view = controller.pick10Paused();

		verify(wordService).pick5Paused();
		assertThat(view).isEqualTo("redirect:/statistic");
	}

	@Test
	void train_shouldAddAttributesAndReturnTrainView() {
		WordDto word = new WordDto();
		word.setState(WordDto.StateEnum.STAGE_1);
		word.setEnglishCnt(2);
		word.setUkrainianCnt(3);

		StateLimitDto stateLimit = new StateLimitDto();
		stateLimit.setAttempt(5);
		stateLimit.setColor("green");

		when(wordService.getWord()).thenReturn(word);
		when(wordService.getStateLimit(WordDto.StateEnum.STAGE_1.getValue())).thenReturn(stateLimit);

		String view = controller.train(model);

		verify(model).addAttribute("word", word);
		verify(model).addAttribute("stateColor", "green");
		verify(model).addAttribute(eq("progressPercent"), anyDouble());
		verify(model).addAttribute("mode", "train");
		assertThat(view).isEqualTo("train");
	}

	@Test
	void train_shouldRedirectToAddWhenNoWordIsAvailable() {
		when(wordService.getWord()).thenReturn(null);

		String view = controller.train(model);

		assertThat(view).isEqualTo("redirect:/add");
		verifyNoInteractions(model);
	}

	@Test
	void randomOneWord_shouldAddWordAndReturnRandomOneView() {
		WordDto word = new WordDto();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getServletPath()).thenReturn("/randomOne");
		when(dictionaryService.getNewWord()).thenReturn(word);

		String view = controller.randomOneWord(model, request);

		verify(model).addAttribute("word", word);
		verify(model).addAttribute("returnUrl", "/randomOne");
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void findWordToSuggest_shouldDefaultLevelToOne() {
		WordDto word = new WordDto();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getServletPath()).thenReturn("/findWordToSuggest");
		when(dictionaryService.findWordToSuggest(1)).thenReturn(word);

		String view = controller.findWordToSuggest(null, model, request);

		verify(model).addAttribute("word", word);
		verify(model).addAttribute("level", 1);
		verify(model).addAttribute("returnUrl", "/findWordToSuggest");
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void random_shouldPopulateModelAndTriggerAsyncRefresh() {
		WordDto queuedWord = new WordDto();
		queuedWord.setEnglish("queue");
		var words = new ArrayList<WordDto>();
		words.add(queuedWord);
		WordDto refreshedWord = new WordDto();
		HttpServletRequest request = mock(HttpServletRequest.class);
		OidcUser user = mock(OidcUser.class);
		when(request.getServletPath()).thenReturn("/random");
		when(user.getClaims()).thenReturn(Map.of("userId", 42L));
		when(dictionaryService.isUpdating(42L)).thenReturn(false);
		when(dictionaryService.getWords(42L)).thenReturn(words);
		when(tokenService.getAccessToken()).thenReturn("access-token");
		when(dictionaryService.getNewWordsAsync(42L, "access-token"))
				.thenReturn(CompletableFuture.completedFuture(java.util.List.of(refreshedWord)));

		String view = controller.randomWord(user, model, request);

		verify(tokenService).getAccessToken();
		verify(dictionaryService).getNewWordsAsync(42L, "access-token");
		verify(model).addAttribute("word", queuedWord);
		verify(model).addAttribute("randomListSize", 0);
		verify(model).addAttribute("returnUrl", "/random");
		assertThat(view).isEqualTo("word-form");
	}

	@Test
	void retro_shouldRedirectToAddWhenWordIsMissing() {
		OidcUser user = mock(OidcUser.class);
		when(user.getClaims()).thenReturn(Map.of("userId", 42L));
		when(wordService.getRetroWord(42L)).thenReturn(null);

		String view = controller.retro(user, model);

		assertThat(view).isEqualTo("redirect:/add");
		verifyNoInteractions(model);
	}

	@Test
	void retro_shouldAddWordAndReturnTrainView() {
		WordDto word = new WordDto();
		OidcUser user = mock(OidcUser.class);
		when(user.getClaims()).thenReturn(Map.of("userId", 42L));
		when(wordService.getRetroWord(42L)).thenReturn(word);

		String view = controller.retro(user, model);

		verify(model).addAttribute("word", word);
		verify(model).addAttribute("mode", "retro");
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
