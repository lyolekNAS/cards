package org.sav.fornas.cards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class DictionaryService {

	private final DictionaryControllerApi dictionaryControllerApi;
	private final DictionaryControllerApi dictionaryControllerApiAsync;

	private final Map<String, List<WordDto>> listCache = new ConcurrentHashMap<>();
	private final Map<String, AtomicBoolean> stateCache = new ConcurrentHashMap<>();


	public List<WordDto> getWords(String key) {
		return Objects.requireNonNullElse(listCache.get(key), List.of());
	}
	public boolean isUpdating(String key) {
		return stateCache.computeIfAbsent(key, k -> new AtomicBoolean(false)).get();
	}

	public List<WordDto> getNewWords(){
		log.debug(">>> getNewWords()");
		return dictionaryControllerApi.getNewWords();
	}

	public WordDto getNewWord(){
		log.debug(">>> getNewWord()");
		return dictionaryControllerApi.getNewWord();
	}

	@Async
	public CompletableFuture<List<WordDto>> getNewWordsAsync(String key, String token){
		log.debug(">>> updateWordsAsync()");

		AtomicBoolean flag = stateCache.computeIfAbsent(key, k -> new AtomicBoolean(false));

		if (!flag.compareAndSet(false, true)) {
			log.debug(">>> update already in progress for key {}", key);
			return CompletableFuture.completedFuture(null);
		}

		try {
			dictionaryControllerApiAsync.getApiClient().addDefaultHeader("Authorization", "Bearer " + token);
			List<WordDto> newWords = dictionaryControllerApiAsync.getNewWords();

			log.debug(">>> fetched {} words", newWords.size());

			listCache.compute(key, (k, oldList) -> {
				if (oldList == null)
					return newWords;
				oldList.addAll(newWords);
				return oldList;
			});

			log.debug(">>> cache updated for {}", key);

			return CompletableFuture.completedFuture(newWords);

		} finally {
			flag.set(false);  // завжди скидаємо стан
		}
	}
}
