package org.sav.fornas.cards.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.service.WordService;
import org.sav.fornas.dto.cards.WordDto;
import org.sav.fornas.dto.cards.TrainedWordDto;
import org.sav.fornas.dto.cards.WordLangDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WordController {

	private final WordService wordService;

	@GetMapping("/words")
	public String viewInfo(Model model) {
		List<WordDto> words = wordService.getWordsByUser();
		model.addAttribute("words", words);
		return "words";
	}

	@GetMapping("/edit")
	public String showForm(Model model, @RequestParam(name = "w", defaultValue = "") String w) {
		WordDto word = wordService.findWord(w.toLowerCase());
		model.addAttribute("word", word);
		return "word-form";
	}

	@GetMapping("/add")
	public String addWord() {
		return "add-word";
	}

	@PostMapping("/save")
	public String saveWord(@ModelAttribute WordDto wordDto) {
		WordDto newWord = wordService.saveWord(wordDto);

		return "redirect:/edit?w=" + newWord.getEnglish();
	}

	@GetMapping("/delete")
	public String deleteWord(@RequestParam("id") Long id) {
		wordService.deleteWord(id);
		return "redirect:/words";
	}

	@GetMapping("/train")
	public String train(Model model) {
		WordDto word = wordService.getWord();
		if(word == null){
			return "redirect:/add";
		}
		model.addAttribute("word", word);
		model.addAttribute("stateLimit", wordService.getStateLimit(word.getState().getId()));
		return "train";
	}

	@GetMapping("/trained")
	public String setTrained(@ModelAttribute TrainedWordDto trainedWordDto) {
		log.info(">>> trainedWord={}", trainedWordDto);
		wordService.setTrained(trainedWordDto);
		return "redirect:/train";
	}
}
