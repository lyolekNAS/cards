package org.sav.fornas.cards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dto.cards.StateLimitDto;
import org.sav.fornas.dto.cards.StatisticDto;
import org.sav.fornas.dto.cards.TrainedWordDto;
import org.sav.fornas.dto.cards.WordDto;
import org.sav.fornas.dto.google.TranslationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.nimbus.State;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WordService {

	private final RestTemplate jwtRestTemplate;
	private final RestTemplate gTranslateRestTemplate;

	public List<WordDto> getWordsByUser(){
		log.debug(">>> getWordsByUser()");
		return jwtRestTemplate.exchange("/word/user/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<WordDto>>(){}).getBody();
	}

	public WordDto saveWord(WordDto wordDto){
		log.debug(">>> saveWord({}})", wordDto);
		return jwtRestTemplate.postForObject("/word/save", wordDto, WordDto.class);
	}

	public void deleteWord(Long id){
		log.debug(">>> deleteWord({}})", id);
		jwtRestTemplate.delete("/word/delete?id=" + id);
	}

	public WordDto findWord(String w){
		WordDto word;
		if(!w.isEmpty()) {
			word = jwtRestTemplate.getForObject("/word/find?w=" + w, WordDto.class);
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
		return jwtRestTemplate.getForObject("/word/train", WordDto.class);
	}

	public StatisticDto getStatistics(){
		return jwtRestTemplate.getForObject("/word/statistic", StatisticDto.class);
	}
	public StateLimitDto getStateLimit(int id){
		return jwtRestTemplate.getForObject("/state/id/" + id, StateLimitDto.class);
	}

	public void setTrained(TrainedWordDto trainedWordDto){
		jwtRestTemplate.postForObject("/word/trained", trainedWordDto, String.class);
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
