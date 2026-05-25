package org.sav.fornas.cards.service;

import org.junit.jupiter.api.Test;
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;
import org.sav.fornas.cards.client.cardsback.model.WordDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryServiceTest {

	@Test
	void getWords_shouldReturnEmptyListWhenCacheDoesNotContainKey() {
		DictionaryService service = new DictionaryService(new StubDictionaryControllerApi(), new StubDictionaryControllerApi());

		List<WordDto> result = service.getWords(1L);

		assertThat(result).isEmpty();
	}

	@Test
	void getNewWords_shouldDelegateToSyncApi() {
		StubDictionaryControllerApi syncApi = new StubDictionaryControllerApi();
		syncApi.newWordsResponses.add(new ArrayList<>(List.of(word(1L, "cat"))));
		DictionaryService service = new DictionaryService(syncApi, new StubDictionaryControllerApi());

		List<WordDto> result = service.getNewWords();

		assertThat(syncApi.getNewWordsCalls).isEqualTo(1);
		assertThat(result).extracting(WordDto::getEnglish).containsExactly("cat");
	}

	@Test
	void getNewWord_shouldDelegateToSyncApi() {
		StubDictionaryControllerApi syncApi = new StubDictionaryControllerApi();
		syncApi.newWordResponse = word(7L, "table");
		DictionaryService service = new DictionaryService(syncApi, new StubDictionaryControllerApi());

		WordDto result = service.getNewWord();

		assertThat(syncApi.getNewWordCalls).isEqualTo(1);
		assertThat(result.getEnglish()).isEqualTo("table");
	}

	@Test
	void enrichWithExamples_shouldDelegateToSyncApi() {
		StubDictionaryControllerApi syncApi = new StubDictionaryControllerApi();
		syncApi.enrichResponse = word(10L, "dog");
		DictionaryService service = new DictionaryService(syncApi, new StubDictionaryControllerApi());

		WordDto result = service.enrichWithExamples("dog");

		assertThat(syncApi.enrichCalls).isEqualTo(1);
		assertThat(syncApi.lastEnrichArg).isEqualTo("dog");
		assertThat(result).isSameAs(syncApi.enrichResponse);
	}

	@Test
	void getNewWordsAsync_shouldSetAuthorizationHeaderUpdateCacheAndResetState() {
		StubDictionaryControllerApi asyncApi = new StubDictionaryControllerApi();
		asyncApi.newWordsResponses.add(new ArrayList<>(List.of(word(1L, "cat"), word(2L, "dog"))));
		DictionaryService service = new DictionaryService(new StubDictionaryControllerApi(), asyncApi);

		List<WordDto> result = service.getNewWordsAsync(11L, "token-1").join();

		assertThat(asyncApi.getNewWordsCalls).isEqualTo(1);
		assertThat(asyncApi.apiClient.lastHeaderName).isEqualTo("Authorization");
		assertThat(asyncApi.apiClient.lastHeaderValue).isEqualTo("Bearer token-1");
		assertThat(result).extracting(WordDto::getEnglish).containsExactly("cat", "dog");
		assertThat(service.getWords(11L)).extracting(WordDto::getEnglish).containsExactly("cat", "dog");
		assertThat(service.isUpdating(11L)).isFalse();
	}

	@Test
	void getNewWordsAsync_shouldAppendToExistingCacheOnSubsequentCalls() {
		StubDictionaryControllerApi asyncApi = new StubDictionaryControllerApi();
		asyncApi.newWordsResponses.add(new ArrayList<>(List.of(word(1L, "cat"))));
		asyncApi.newWordsResponses.add(new ArrayList<>(List.of(word(2L, "dog"))));
		DictionaryService service = new DictionaryService(new StubDictionaryControllerApi(), asyncApi);

		service.getNewWordsAsync(21L, "token-1").join();
		service.getNewWordsAsync(21L, "token-2").join();

		assertThat(asyncApi.getNewWordsCalls).isEqualTo(2);
		assertThat(service.getWords(21L)).extracting(WordDto::getEnglish).containsExactly("cat", "dog");
		assertThat(service.isUpdating(21L)).isFalse();
	}

	@Test
	void getNewWordsAsync_shouldReturnNullWhenUpdateAlreadyInProgress() throws Exception {
		StubDictionaryControllerApi asyncApi = new StubDictionaryControllerApi();
		asyncApi.newWordsResponses.add(new ArrayList<>(List.of(word(1L, "cat"))));
		asyncApi.enteredGetNewWordsLatch = new CountDownLatch(1);
		asyncApi.releaseGetNewWordsLatch = new CountDownLatch(1);
		DictionaryService service = new DictionaryService(new StubDictionaryControllerApi(), asyncApi);

		try (var executor = Executors.newSingleThreadExecutor()) {
			CompletableFuture<List<WordDto>> firstCall = CompletableFuture.supplyAsync(
					() -> service.getNewWordsAsync(31L, "token-main").join(),
					executor
			);

			boolean entered = asyncApi.enteredGetNewWordsLatch.await(2, TimeUnit.SECONDS);
			assertThat(entered).isTrue();

			CompletableFuture<List<WordDto>> secondCall = service.getNewWordsAsync(31L, "token-second");
			assertThat(secondCall.isDone()).isTrue();
			assertThat(secondCall.join()).isNull();

			asyncApi.releaseGetNewWordsLatch.countDown();

			assertThat(firstCall.join()).extracting(WordDto::getEnglish).containsExactly("cat");
		}

		assertThat(asyncApi.getNewWordsCalls).isEqualTo(1);
		assertThat(service.getWords(31L)).extracting(WordDto::getEnglish).containsExactly("cat");
		assertThat(service.isUpdating(31L)).isFalse();
	}

	private static WordDto word(Long id, String english) {
		return new WordDto().id(id).english(english);
	}

	private static class StubDictionaryControllerApi extends DictionaryControllerApi {
		private final CapturingApiClient apiClient;
		private final List<List<WordDto>> newWordsResponses = new ArrayList<>();
		private WordDto newWordResponse;
		private WordDto enrichResponse;
		private String lastEnrichArg;
		private int getNewWordsCalls;
		private int getNewWordCalls;
		private int enrichCalls;
		private CountDownLatch enteredGetNewWordsLatch;
		private CountDownLatch releaseGetNewWordsLatch;

		private StubDictionaryControllerApi() {
			this(new CapturingApiClient());
		}

		private StubDictionaryControllerApi(CapturingApiClient apiClient) {
			super(apiClient);
			this.apiClient = apiClient;
		}

		@Override
		public List<WordDto> getNewWords() {
			getNewWordsCalls++;
			awaitLatchesIfNeeded();
			if (newWordsResponses.isEmpty()) {
				return List.of();
			}
			return newWordsResponses.remove(0);
		}

		@Override
		public WordDto getNewWord() {
			getNewWordCalls++;
			return newWordResponse;
		}

		@Override
		public WordDto enrichWithExamples(String word) {
			enrichCalls++;
			lastEnrichArg = word;
			return enrichResponse;
		}

		private void awaitLatchesIfNeeded() {
			if (enteredGetNewWordsLatch == null || releaseGetNewWordsLatch == null) {
				return;
			}
			enteredGetNewWordsLatch.countDown();
			try {
				releaseGetNewWordsLatch.await(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}
	}

	private static class CapturingApiClient extends ApiClient {
		private String lastHeaderName;
		private String lastHeaderValue;

		@Override
		public ApiClient addDefaultHeader(String name, String value) {
			lastHeaderName = name;
			lastHeaderValue = value;
			return super.addDefaultHeader(name, value);
		}
	}
}
