

# WordDto


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **Long** |  |  [optional] |
|**english** | **String** |  |  [optional] |
|**ukrainian** | **String** |  |  [optional] |
|**description** | **String** |  |  [optional] |
|**userId** | **Long** |  |  [optional] |
|**englishCnt** | **Integer** |  |  [optional] |
|**ukrainianCnt** | **Integer** |  |  [optional] |
|**lastTrain** | **OffsetDateTime** |  |  [optional] |
|**nextTrain** | **OffsetDateTime** |  |  [optional] |
|**lang** | [**LangEnum**](#LangEnum) |  |  [optional] |
|**state** | [**StateEnum**](#StateEnum) |  |  [optional] |
|**dictWordId** | **Long** |  |  [optional] |



## Enum: LangEnum

| Name | Value |
|---- | -----|
| EN | &quot;EN&quot; |
| UA | &quot;UA&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| PAUSED | &quot;PAUSED&quot; |
| STAGE_1 | &quot;STAGE_1&quot; |
| STAGE_2 | &quot;STAGE_2&quot; |
| STAGE_3 | &quot;STAGE_3&quot; |
| STAGE_4 | &quot;STAGE_4&quot; |
| STAGE_5 | &quot;STAGE_5&quot; |
| STAGE_6 | &quot;STAGE_6&quot; |
| STAGE_7 | &quot;STAGE_7&quot; |
| DONE | &quot;DONE&quot; |



