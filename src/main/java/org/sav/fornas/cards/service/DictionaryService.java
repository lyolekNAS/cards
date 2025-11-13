package org.sav.fornas.cards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.model.DictWord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DictionaryService {

	private final DictionaryControllerApi dictionaryControllerApi;

	public List<DictWord> getNewWords(){
		log.debug(">>> getNewWords()");
		return dictionaryControllerApi.getNewWords();
	}
}
