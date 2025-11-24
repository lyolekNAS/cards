package org.sav.fornas.cards.client.cardsback.api;

import org.sav.fornas.cards.client.cardsback.ApiClient;

import org.sav.fornas.cards.client.cardsback.model.StatisticDto;
import org.sav.fornas.cards.client.cardsback.model.TrainedWordDto;
import org.sav.fornas.cards.client.cardsback.model.Word;
import org.sav.fornas.cards.client.cardsback.model.WordDto;
import org.sav.fornas.cards.client.cardsback.model.WordsPageDtoWordDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-11-24T17:43:25.791512854+02:00[Europe/Kyiv]")
public class WordControllerApi {
    private ApiClient apiClient;

    public WordControllerApi() {
        this(new ApiClient());
    }

    public WordControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param wordDto  (required)
     * @return WordDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public WordDto addWord(WordDto wordDto) throws RestClientException {
        return addWordWithHttpInfo(wordDto).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param wordDto  (required)
     * @return ResponseEntity&lt;WordDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WordDto> addWordWithHttpInfo(WordDto wordDto) throws RestClientException {
        Object localVarPostBody = wordDto;
        
        // verify the required parameter 'wordDto' is set
        if (wordDto == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'wordDto' when calling addWord");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<WordDto> localReturnType = new ParameterizedTypeReference<WordDto>() {};
        return apiClient.invokeAPI("/api/word/save", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id  (required)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String deleteWord(Long id) throws RestClientException {
        return deleteWordWithHttpInfo(id).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id  (required)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> deleteWordWithHttpInfo(Long id) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling deleteWord");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "id", id));


        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<String> localReturnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI("/api/word/delete", HttpMethod.DELETE, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param w  (required)
     * @return WordDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public WordDto findWord(String w) throws RestClientException {
        return findWordWithHttpInfo(w).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param w  (required)
     * @return ResponseEntity&lt;WordDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WordDto> findWordWithHttpInfo(String w) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'w' is set
        if (w == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'w' when calling findWord");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "w", w));


        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<WordDto> localReturnType = new ParameterizedTypeReference<WordDto>() {};
        return apiClient.invokeAPI("/api/word/find", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return WordDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public WordDto findWordToTrain() throws RestClientException {
        return findWordToTrainWithHttpInfo().getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;WordDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WordDto> findWordToTrainWithHttpInfo() throws RestClientException {
        Object localVarPostBody = null;
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<WordDto> localReturnType = new ParameterizedTypeReference<WordDto>() {};
        return apiClient.invokeAPI("/api/word/train", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return List&lt;Word&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Word> getAll() throws RestClientException {
        return getAllWithHttpInfo().getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;List&lt;Word&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Word>> getAllWithHttpInfo() throws RestClientException {
        Object localVarPostBody = null;
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<List<Word>> localReturnType = new ParameterizedTypeReference<List<Word>>() {};
        return apiClient.invokeAPI("/api/word/all", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param page  (optional, default to 0)
     * @param size  (optional, default to 20)
     * @param state  (optional, default to )
     * @return WordsPageDtoWordDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public WordsPageDtoWordDto getAllByUser(Integer page, Integer size, String state) throws RestClientException {
        return getAllByUserWithHttpInfo(page, size, state).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param page  (optional, default to 0)
     * @param size  (optional, default to 20)
     * @param state  (optional, default to )
     * @return ResponseEntity&lt;WordsPageDtoWordDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WordsPageDtoWordDto> getAllByUserWithHttpInfo(Integer page, Integer size, String state) throws RestClientException {
        Object localVarPostBody = null;
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "size", size));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "state", state));


        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<WordsPageDtoWordDto> localReturnType = new ParameterizedTypeReference<WordsPageDtoWordDto>() {};
        return apiClient.invokeAPI("/api/word/user/all", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return StatisticDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public StatisticDto getStatistic() throws RestClientException {
        return getStatisticWithHttpInfo().getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;StatisticDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StatisticDto> getStatisticWithHttpInfo() throws RestClientException {
        Object localVarPostBody = null;
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<StatisticDto> localReturnType = new ParameterizedTypeReference<StatisticDto>() {};
        return apiClient.invokeAPI("/api/word/statistic", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param trainedWordDto  (required)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String processTrainedWord(TrainedWordDto trainedWordDto) throws RestClientException {
        return processTrainedWordWithHttpInfo(trainedWordDto).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param trainedWordDto  (required)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> processTrainedWordWithHttpInfo(TrainedWordDto trainedWordDto) throws RestClientException {
        Object localVarPostBody = trainedWordDto;
        
        // verify the required parameter 'trainedWordDto' is set
        if (trainedWordDto == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'trainedWordDto' when calling processTrainedWord");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<String> localReturnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI("/api/word/trained", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
