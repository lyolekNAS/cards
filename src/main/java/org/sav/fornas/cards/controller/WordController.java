package org.sav.fornas.cards.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.model.*;
import org.sav.fornas.cards.security.TokenService;
import org.sav.fornas.cards.service.DictionaryService;
import org.sav.fornas.cards.service.WordService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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
	public String showForm(Model model, @RequestParam(name = "w", defaultValue = "") String w) {
		WordDto word = wordService.findWord(w.toLowerCase());
		if (word.getState() == null) {
			word.setState(WordDto.StateEnum.STAGE_1);
		}
		model.addAttribute("word", word);
		return "word-form";
	}

	@GetMapping("/random")
	public String randomWord(HttpSession session, Model model,
							 @RequestParam(name = "w", defaultValue = "") String w) {

		String key = (String) session.getAttribute("wordsKey");
		if(key == null){
			key = UUID.randomUUID().toString();
			session.setAttribute("wordsKey", key);
		}
		log.debug(">>> key={}", key);
		boolean isUpdating = dictionaryService.isUpdating(key);

		log.debug(">>> isUpdating={}", isUpdating);
		List<WordDto> words = dictionaryService.getWords(key);
		if(words.size() <= 3 && !isUpdating) {
			log.debug(">>> initiating update");

			String token = tokenService.getAccessToken();

			String localKey = key;
			dictionaryService.getNewWordsAsync(key, token)
					.thenRun(() -> {
						log.debug(">>> async update finished for key {}", localKey);
					});

		}

		WordDto word = !words.isEmpty() ? words.removeFirst() : null;
		model.addAttribute("word", word);
		model.addAttribute("randomListSize", words.size());
		return "word-form";
	}

	@GetMapping("/add")
	public String addWord() {
		return "add-word";
	}

	@PostMapping("/save")
	public String saveWord(@ModelAttribute WordDto wordDto) {

		log.debug(">>> saveWord={}", wordDto);
		WordDto newWord = wordService.saveWord(wordDto);
		return "redirect:/edit?w=" + newWord.getEnglish();
	}

	@PostMapping("/setSkipped")
	public String setSkipped(@RequestParam Long dictWordId) {
		wordService.setMark(dictWordId, "SKIP");
		return "redirect:/random";
	}

	@PostMapping("/setKnown")
	public String setKnown(@RequestParam Long dictWordId) {
		wordService.setMark(dictWordId, "KNOWN");
		return "redirect:/random";
	}

	@GetMapping("/delete")
	public String deleteWord(@RequestParam("id") Long id) {
		wordService.deleteWord(id);
		return "redirect:/words";
	}

	@GetMapping("/statistic")
	public String statistic(Model model) {
		StatisticDto statisticDto = wordService.getStatistics();
		log.debug(">>> statistic={}", statisticDto);
		model.addAttribute("statistics", statisticDto);
		return "statistic";
	}

	@GetMapping("/train")
	public String train(Model model) {
		WordDto word = wordService.getWord();
		if(word == null){
			return "redirect:/add";
		}
		StateLimitDto stateLimit = wordService.getStateLimit(word.getState().getValue());
		int minCnt = Math.min(word.getEnglishCnt(), word.getUkrainianCnt());
		double progressPercent = (double) (minCnt + 1) * 100 / (stateLimit.getAttempt() + 1);

		model.addAttribute("progressPercent", progressPercent);
		model.addAttribute("word", word);
		model.addAttribute("stateColor", stateLimit.getColor());
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
