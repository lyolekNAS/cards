package org.sav.fornas.cards.client.cardsback.api;

import org.sav.fornas.cards.client.cardsback.ApiClient;

import org.sav.fornas.cards.client.cardsback.model.StateLimitDto;

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

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-11-22T14:32:06.688142937+02:00[Europe/Kyiv]")
public class StateLimitControllerApi {
    private ApiClient apiClient;

    public StateLimitControllerApi() {
        this(new ApiClient());
    }

    public StateLimitControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all state limits
     * 
     * <p><b>200</b> - OK
     * @return List&lt;StateLimitDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<StateLimitDto> getAllStateLimits() throws RestClientException {
        return getAllStateLimitsWithHttpInfo().getBody();
    }

    /**
     * Get all state limits
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;List&lt;StateLimitDto&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<StateLimitDto>> getAllStateLimitsWithHttpInfo() throws RestClientException {
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

        ParameterizedTypeReference<List<StateLimitDto>> localReturnType = new ParameterizedTypeReference<List<StateLimitDto>>() {};
        return apiClient.invokeAPI("/api/state/all", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param state  (required)
     * @return StateLimitDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public StateLimitDto getById(String state) throws RestClientException {
        return getByIdWithHttpInfo(state).getBody();
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param state  (required)
     * @return ResponseEntity&lt;StateLimitDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StateLimitDto> getByIdWithHttpInfo(String state) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'state' is set
        if (state == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'state' when calling getById");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("state", state);

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

        ParameterizedTypeReference<StateLimitDto> localReturnType = new ParameterizedTypeReference<StateLimitDto>() {};
        return apiClient.invokeAPI("/api/state/id/{state}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
