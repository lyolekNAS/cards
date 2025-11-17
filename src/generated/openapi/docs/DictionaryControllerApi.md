# DictionaryControllerApi

All URIs are relative to *http://localhost:8052*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getNewWords**](DictionaryControllerApi.md#getNewWords) | **GET** /api/dict/getNewWords |  |
| [**getWord**](DictionaryControllerApi.md#getWord) | **GET** /api/dict/getWord/{word} |  |
| [**setMarkOnWord**](DictionaryControllerApi.md#setMarkOnWord) | **POST** /api/dict/setMark |  |



## getNewWords

> List&lt;WordDto&gt; getNewWords()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        DictionaryControllerApi apiInstance = new DictionaryControllerApi(defaultClient);
        try {
            List<WordDto> result = apiInstance.getNewWords();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DictionaryControllerApi#getNewWords");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**List&lt;WordDto&gt;**](WordDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getWord

> DictWord getWord(word)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        DictionaryControllerApi apiInstance = new DictionaryControllerApi(defaultClient);
        String word = "word_example"; // String | 
        try {
            DictWord result = apiInstance.getWord(word);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DictionaryControllerApi#getWord");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **word** | **String**|  | |

### Return type

[**DictWord**](DictWord.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## setMarkOnWord

> setMarkOnWord(wordId, mark)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.DictionaryControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        DictionaryControllerApi apiInstance = new DictionaryControllerApi(defaultClient);
        Long wordId = 56L; // Long | 
        String mark = "mark_example"; // String | 
        try {
            apiInstance.setMarkOnWord(wordId, mark);
        } catch (ApiException e) {
            System.err.println("Exception when calling DictionaryControllerApi#setMarkOnWord");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **wordId** | **Long**|  | |
| **mark** | **String**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

