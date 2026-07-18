package org.sav.fornas.cards.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.model.*;
import org.sav.fornas.cards.security.TokenService;
import org.sav.fornas.cards.service.DictionaryService;
import org.sav.fornas.cards.service.WordService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.OffsetDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WordController {

	private final WordService wordService;
	private final DictionaryService dictionaryService;
	private final TokenService tokenService;

	@GetMapping("/words")
	public String viewInfo(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "") String state,
			Model model) {

		WordsPageDtoWordDto words = wordService.getWordsByUser(page, size, state);
		model.addAttribute("words", words);
		return "words";
	}

	@GetMapping("/edit")
	public String showForm(Model model, @RequestParam(name = "w", defaultValue = "") String w, HttpServletRequest request) {
		WordDto word = wordService.findWord(w.toLowerCase());
		if (word.getState() == null) {
			word.setState(WordDto.StateEnum.STAGE_1);
		}
		model.addAttribute("word", word);
		model.addAttribute("returnUrl", request.getServletPath() + "?w=" + w);
		return "word-form";
	}

	@GetMapping("/edit-card")
	public String showCardForm(Model model, @RequestParam(name = "w", defaultValue = "") String w, HttpServletRequest request) {
		WordDto word = wordService.findCardWord(w.toLowerCase());
		if (word.getState() == null) {
			word.setState(WordDto.StateEnum.STAGE_1);
		}
		model.addAttribute("word", word);
		model.addAttribute("returnUrl", "/edit?w=" + w);
		return "word-form";
	}

	@GetMapping("/reset")
	public String resetWord(@RequestParam(name = "id", defaultValue = "") Long id, @RequestParam(name = "w", defaultValue = "") String w) {
		dictionaryService.resetWord(id);
		return "redirect:/edit?w=" + w;
	}

	@GetMapping("/random")
	public String randomWord(@AuthenticationPrincipal OidcUser user, Model model, HttpServletRequest request) {

		Long key = (Long) user.getClaims().get("userId");
		log.debug(">>> key={}", key);
		boolean isUpdating = dictionaryService.isUpdating(key);

		log.debug(">>> isUpdating={}", isUpdating);
		List<WordDto> words = dictionaryService.getWords(key);
		if(words.size() <= 3 && !isUpdating) {
			log.debug(">>> initiating update");

			String token = tokenService.getAccessToken();

			dictionaryService.getNewWordsAsync(key, token)
					.thenRun(() -> log.debug(">>> async update finished for key {}", key));

		}

		WordDto word = !words.isEmpty() ? words.removeFirst() : null;
		model.addAttribute("word", word);
		model.addAttribute("randomListSize", words.size());
		model.addAttribute("returnUrl", request.getServletPath());
		return "word-form";
	}

	@GetMapping("/randomOne")
	public String randomOneWord(Model model, HttpServletRequest request) {

		WordDto word = dictionaryService.getNewWord();
		model.addAttribute("word", word);
		model.addAttribute("returnUrl", request.getServletPath());
		return "word-form";
	}

	@GetMapping(value = {"/findWordToSuggest", "/findWordToSuggest/{level}"})
	public String findWordToSuggest(@PathVariable(required = false) Integer level, Model model, HttpServletRequest request) {
		level = level == null ? 1 : level;
		WordDto word = dictionaryService.findWordToSuggest(level);
		model.addAttribute("word", word);
		model.addAttribute("level", level);
		model.addAttribute("returnUrl", request.getServletPath());
		return "word-form";
	}

	@GetMapping("/pick5Paused")
	public String pick10Paused() {
		wordService.pick5Paused();
		return "redirect:/statistic";
	}

	@GetMapping("/add")
	public String addWord() {
		return "add-word";
	}

	@PostMapping("/save")
	public String saveWord(@ModelAttribute WordDto wordDto, @RequestParam String returnUrl) {

		log.debug(">>> saveWord={}", wordDto);
		WordDto newWord = wordService.saveWord(wordDto);
		return "redirect:" + returnUrl;
	}

	@PostMapping("/setSkipped")
	public String setSkipped(@RequestParam Long dictWordId, @RequestParam String returnUrl) {
		wordService.setMark(dictWordId, "SKIP");
		return "redirect:" + returnUrl;
	}

	@PostMapping("/setKnown")
	public String setKnown(@RequestParam Long dictWordId, @RequestParam String returnUrl) {
		wordService.setMark(dictWordId, "KNOWN");
		return "redirect:" + returnUrl;
	}

	@GetMapping("/delete")
	public String deleteWord(@RequestParam("id") Long id) {
		wordService.deleteWord(id);
		return "redirect:/words";
	}

	@GetMapping("/statistic")
	public String statistic(Model model) {
		StatisticDto statisticDto = wordService.getStatistics();
		List<StatisticDictionaryDto> dicStatisticDto = wordService.getDictStatistics();
		log.debug(">>> statistic={}", statisticDto);
		model.addAttribute("statistics", statisticDto);
		model.addAttribute("dicStatistics", dicStatisticDto);
		return "statistic";
	}

	@GetMapping("/train")
	public String train(Model model) {
		WordDto word = wordService.getWord();
		if(word == null){
			return "redirect:/add";
		}
		StateLimitDto stateLimit = wordService.getStateLimit(word.getState().getValue());
		int minCnt = word.getEnglishCnt() + word.getUkrainianCnt();
		double progressPercent = (double) (minCnt + 1) * 100 / (stateLimit.getAttempt() * 2);

		model.addAttribute("progressPercent", progressPercent);
		model.addAttribute("word", word);
		model.addAttribute("stateColor", stateLimit.getColor());
		model.addAttribute("mode", "train");
		return "train";
	}

	@GetMapping("/retro")
	public String retro(@AuthenticationPrincipal OidcUser user, Model model) {
		Long key = (Long) user.getClaims().get("userId");
		WordDto word = wordService.getRetroWord(key);
		if(word == null){
			return "redirect:/add";
		}
		model.addAttribute("word", word);
		model.addAttribute("mode", "retro");
		return "train";
	}

	@GetMapping("/trained")
	public String setTrained(@ModelAttribute TrainedWordDto trainedWordDto) {
		log.debug(">>> trainedWord={}", trainedWordDto);
		wordService.setTrained(trainedWordDto);
		return "redirect:/train";
	}



	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(OffsetDateTime.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				if (text == null || text.isEmpty()) {
					setValue(null);
				} else {
					setValue(OffsetDateTime.parse(text));
				}
			}
		});
	}
}
