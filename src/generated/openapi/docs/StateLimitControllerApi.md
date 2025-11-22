# StateLimitControllerApi

All URIs are relative to *http://localhost:8052*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getAllStateLimits**](StateLimitControllerApi.md#getAllStateLimits) | **GET** /api/state/all | Get all state limits |
| [**getById**](StateLimitControllerApi.md#getById) | **GET** /api/state/id/{state} |  |



## getAllStateLimits

> List&lt;StateLimitDto&gt; getAllStateLimits()

Get all state limits

### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        StateLimitControllerApi apiInstance = new StateLimitControllerApi(defaultClient);
        try {
            List<StateLimitDto> result = apiInstance.getAllStateLimits();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling StateLimitControllerApi#getAllStateLimits");
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

[**List&lt;StateLimitDto&gt;**](StateLimitDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## getById

> StateLimitDto getById(state)



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.StateLimitControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        StateLimitControllerApi apiInstance = new StateLimitControllerApi(defaultClient);
        String state = "PAUSED"; // String | 
        try {
            StateLimitDto result = apiInstance.getById(state);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling StateLimitControllerApi#getById");
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
| **state** | **String**|  | [enum: PAUSED, STAGE_1, STAGE_2, STAGE_3, STAGE_4, STAGE_5, STAGE_6, STAGE_7, DONE] |

### Return type

[**StateLimitDto**](StateLimitDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

