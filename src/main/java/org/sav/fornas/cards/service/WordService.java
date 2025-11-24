package org.sav.fornas.cards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.sav.fornas.cards.client.cardsback.model.*;
import org.sav.fornas.dto.google.TranslationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class WordService {

	private final RestTemplate gTranslateRestTemplate;
	private final WordControllerApi wordControllerApi;
	private final DictionaryControllerApi dictionaryControllerApi;
	private final StateLimitControllerApi stateLimitControllerApi;

	public WordsPageDtoWordDto getWordsByUser(Integer page, Integer size, String state){
		state = Objects.requireNonNullElse(state, "");
		log.debug(">>> getWordsByUser({})", state);
		return wordControllerApi.getAllByUser(page, size, state);
	}

	public WordDto saveWord(WordDto wordDto){
		log.debug(">>> saveWord({}})", wordDto);
		return wordControllerApi.addWord(wordDto);
	}

	public void setMark(Long wordId, String mark){
		dictionaryControllerApi.setMarkOnWord(wordId, mark);
	}

	public void deleteWord(Long id){
		log.debug(">>> deleteWord({}})", id);
		wordControllerApi.deleteWord(id);
	}

	public WordDto findWord(String w){
		WordDto word;
		if(!w.isEmpty()) {
			word = wordControllerApi.findWord(w);
			log.debug(">>> Word found: {}", word);
			if(word == null){
				word = new WordDto();
				word.setEnglish(w);
				word.setUkrainian(getTranslated(w));
			}
		} else {
			word = new WordDto();
		}
		log.debug(">>> Word returned: {}", word);
		return word;
	}

	public WordDto getWord(){
		WordDto w = wordControllerApi.findWordToTrain();
		if(w != null && w.getDescription() != null) {
			w.description(w.getDescription().replace("\n", "<br/>"));
		}
		return w;
	}

	public StatisticDto getStatistics(){
		return wordControllerApi.getStatistic();
	}

	public StateLimitDto getStateLimit(String value){
		return stateLimitControllerApi.getById(value);
	}

	public List<StateLimitDto> getStateLimits(){
		return stateLimitControllerApi.getAllStateLimits();
	}

	public void setTrained(TrainedWordDto trainedWordDto){
		wordControllerApi.processTrainedWord(trainedWordDto);
	}

	private String getTranslated(String w){
		TranslationResponse resp = gTranslateRestTemplate.postForObject("/v2?target=uk&source=en&q=" + w, null, TranslationResponse.class);
		if(resp != null){
			return resp.getData().getTranslations().getFirst().getTranslatedText();
		} else {
			return "";
		}
	}
}
