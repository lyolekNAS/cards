# DictionaryControllerApi

All URIs are relative to *http://localhost:8052*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getNewWords**](DictionaryControllerApi.md#getNewWords) | **GET** /api/dict/get-new |  |



## getNewWords

> List&lt;DictWord&gt; getNewWords()



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
            List<DictWord> result = apiInstance.getNewWords();
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

[**List&lt;DictWord&gt;**](DictWord.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

