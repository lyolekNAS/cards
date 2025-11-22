# WordControllerApi

All URIs are relative to *http://localhost:8052*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addWord**](WordControllerApi.md#addWord) | **POST** /api/word/save |  |
| [**deleteWord**](WordControllerApi.md#deleteWord) | **DELETE** /api/word/delete |  |
| [**findWord**](WordControllerApi.md#findWord) | **GET** /api/word/find |  |
| [**findWordToTrain**](WordControllerApi.md#findWordToTrain) | **GET** /api/word/train |  |
| [**getAll**](WordControllerApi.md#getAll) | **GET** /api/word/all |  |
| [**getAllByUser**](WordControllerApi.md#getAllByUser) | **GET** /api/word/user/all |  |
| [**getStatistic**](WordControllerApi.md#getStatistic) | **GET** /api/word/statistic |  |
| [**processTrainedWord**](WordControllerApi.md#processTrainedWord) | **POST** /api/word/trained |  |



## addWord

> WordDto addWord(wordDto)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        WordDto wordDto = new WordDto(); // WordDto | 
        try {
            WordDto result = apiInstance.addWord(wordDto);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#addWord");
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
| **wordDto** | [**WordDto**](WordDto.md)|  | |

### Return type

[**WordDto**](WordDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## deleteWord

> String deleteWord(id)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        Long id = 56L; // Long | 
        try {
            String result = apiInstance.deleteWord(id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#deleteWord");
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
| **id** | **Long**|  | |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## findWord

> WordDto findWord(w)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        String w = "w_example"; // String | 
        try {
            WordDto result = apiInstance.findWord(w);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#findWord");
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
| **w** | **String**|  | |

### Return type

[**WordDto**](WordDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## findWordToTrain

> WordDto findWordToTrain()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        try {
            WordDto result = apiInstance.findWordToTrain();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#findWordToTrain");
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

[**WordDto**](WordDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getAll

> List&lt;Word&gt; getAll()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        try {
            List<Word> result = apiInstance.getAll();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#getAll");
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

[**List&lt;Word&gt;**](Word.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getAllByUser

> WordsPageDtoWordDto getAllByUser(page, size, state)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        Integer page = 0; // Integer | 
        Integer size = 20; // Integer | 
        String state = ""; // String | 
        try {
            WordsPageDtoWordDto result = apiInstance.getAllByUser(page, size, state);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#getAllByUser");
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
| **page** | **Integer**|  | [optional] [default to 0] |
| **size** | **Integer**|  | [optional] [default to 20] |
| **state** | **String**|  | [optional] [default to ] |

### Return type

[**WordsPageDtoWordDto**](WordsPageDtoWordDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getStatistic

> StatisticDto getStatistic()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        try {
            StatisticDto result = apiInstance.getStatistic();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#getStatistic");
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

[**StatisticDto**](StatisticDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## processTrainedWord

> String processTrainedWord(trainedWordDto)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.WordControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        WordControllerApi apiInstance = new WordControllerApi(defaultClient);
        TrainedWordDto trainedWordDto = new TrainedWordDto(); // TrainedWordDto | 
        try {
            String result = apiInstance.processTrainedWord(trainedWordDto);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WordControllerApi#processTrainedWord");
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
| **trainedWordDto** | [**TrainedWordDto**](TrainedWordDto.md)|  | |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

