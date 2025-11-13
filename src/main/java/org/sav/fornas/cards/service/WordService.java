package org.sav.fornas.cards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;
import org.sav.fornas.cards.client.cardsback.model.StateLimitDto;
import org.sav.fornas.cards.client.cardsback.model.StatisticDto;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WordService {

	private final RestTemplate gTranslateRestTemplate;
	private final WordControllerApi wordControllerApi;
	private final StateLimitControllerApi stateLimitControllerApi;

	public List<WordDto> getWordsByUser(){
		log.debug(">>> getWordsByUser()");
		return wordControllerApi.getAllByUser();
	}

	public WordDto saveWord(WordDto wordDto){
		log.debug(">>> saveWord({}})", wordDto);
		return wordControllerApi.addWord(wordDto);
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
		return wordControllerApi.findWordToTrain();
	}

	public StatisticDto getStatistics(){
		return wordControllerApi.getStatistic();
	}

	public StateLimitDto getStateLimit(String value){
		return stateLimitControllerApi.getById(value);
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
