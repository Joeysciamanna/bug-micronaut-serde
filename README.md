## Expected behavior

```
List<ResponseDTO> list = httpClient.toBlocking().retrieve(
    HttpRequest.POST("http://127.0.0.1:8080/", List.of(new RequestDTO("request"))),
    Argument.listOf(ResponseDTO.class)
);
```
When executing the above Code, a request should be made with the given List of RequestDTOs retrieving a List of ResponseDTOs

## Actual Behaviour
A ClassCastException is thrown
```
java.lang.ClassCastException: class com.example.Test$RequestDTO cannot be cast to class com.example.Test$ResponseDTO (com.example.Test$RequestDTO and com.example.Test$ResponseDTO are in unnamed module of loader 'app')
	at com.example.$Test$ResponseDTO$Introspection.dispatchOne(Unknown Source)
	at io.micronaut.inject.beans.AbstractInitializableBeanIntrospection$BeanPropertyImpl.getUnsafe(AbstractInitializableBeanIntrospection.java:461)
	at io.micronaut.serde.support.serializers.SerBean$PropSerProperty.get(SerBean.java:416)
	at io.micronaut.serde.support.serializers.CustomizedObjectSerializer.serialize(CustomizedObjectSerializer.java:67)
	at io.micronaut.serde.support.serializers.CustomizedIterableSerializer.serialize(CustomizedIterableSerializer.java:50)
	at io.micronaut.serde.support.serializers.CustomizedIterableSerializer.serialize(CustomizedIterableSerializer.java:32)
	at io.micronaut.serde.jackson.JacksonJsonMapper.writeValue(JacksonJsonMapper.java:108)
	at io.micronaut.serde.jackson.JacksonJsonMapper.writeValue(JacksonJsonMapper.java:181)
	at io.micronaut.json.codec.MapperMediaTypeCodec.encode(MapperMediaTypeCodec.java:219)
...
```

## Steps To Reproduce

Following dependencies have been used:
- Micronaut version 3.9.1
- implementation 'jakarta.annotation:jakarta.annotation-api'
- implementation 'io.micronaut:micronaut-http-client'
- annotationProcessor 'io.micronaut.serde:micronaut-serde-processor'
- implementation 'io.micronaut.serde:micronaut-serde-jackson'
- implementation 'io.micronaut.serde:micronaut-serde-api'
- compileOnly 'com.fasterxml.jackson.core:jackson-databind'

and following Controller
```
@Controller
public class Test {

    @Inject
    private HttpClient httpClient;


    @Post
    public List<ResponseDTO> handle(@Body List<RequestDTO> requestDTOs) {
        return List.of(new ResponseDTO("response"));
    }

    @Scheduled(initialDelay = "5s")
    public void test() {
        List<ResponseDTO> list = httpClient.toBlocking().retrieve(
                HttpRequest.POST("http://127.0.0.1:8080/", List.of(new RequestDTO("request"))),
                Argument.listOf(ResponseDTO.class)
        );
        System.out.println(list.get(0));
    }

    @Serdeable
    public record RequestDTO(String string) {

    }

    @Serdeable
    public record ResponseDTO(String string) {

    }

}
```

When using different "raw" types for request and response, it works. However if both are of the same type e.g. List, the Exception occurs.  
A quick workaround would be to use a Set instead of a List for the request body

__... or just clone this repository ...__


## Environment 
- JDK version 17 (Also tested on 19)
- Gradle version 7.6.1 (Also tested on 8.1.1)
- Micronaut version 3.9.1