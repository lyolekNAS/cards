package org.sav.fornas.cards.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.model.DictWord;
import org.sav.fornas.cards.service.DictionaryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DictionaryController {

	private final DictionaryService dictionaryService;


	@RequestMapping("/get-new")
	public List<DictWord> getNewWords(){
		return dictionaryService.getNewWords();
	}
}
