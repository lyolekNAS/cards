# MeControllerApi

All URIs are relative to *http://localhost:8052*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**meAdmin**](MeControllerApi.md#meAdmin) | **GET** /api/admin/me |  |
| [**meAll**](MeControllerApi.md#meAll) | **GET** /api/all/me |  |
| [**meJunior**](MeControllerApi.md#meJunior) | **GET** /api/junior/me |  |



## meAdmin

> Map&lt;String, Object&gt; meAdmin()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.MeControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        MeControllerApi apiInstance = new MeControllerApi(defaultClient);
        try {
            Map<String, Object> result = apiInstance.meAdmin();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MeControllerApi#meAdmin");
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

**Map&lt;String, Object&gt;**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## meAll

> Map&lt;String, Object&gt; meAll()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.MeControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        MeControllerApi apiInstance = new MeControllerApi(defaultClient);
        try {
            Map<String, Object> result = apiInstance.meAll();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MeControllerApi#meAll");
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

**Map&lt;String, Object&gt;**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## meJunior

> Map&lt;String, Object&gt; meJunior()



### Example

```java
// Import classes:
import org.sav.fornas.cards.client.cardsback.ApiClient;
import org.sav.fornas.cards.client.cardsback.ApiException;
import org.sav.fornas.cards.client.cardsback.Configuration;
import org.sav.fornas.cards.client.cardsback.models.*;
import org.sav.fornas.cards.client.cardsback.api.MeControllerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8052");

        MeControllerApi apiInstance = new MeControllerApi(defaultClient);
        try {
            Map<String, Object> result = apiInstance.meJunior();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MeControllerApi#meJunior");
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

**Map&lt;String, Object&gt;**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

