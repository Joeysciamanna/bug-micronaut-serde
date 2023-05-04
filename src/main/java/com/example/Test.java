package com.example;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.HttpClient;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;

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
        Set<ResponseDTO> list = httpClient.toBlocking().retrieve(
                HttpRequest.POST("http://127.0.0.1:8080/", Set.of(new RequestDTO("request"))),
                Argument.setOf(ResponseDTO.class)
        );
        System.out.println(list.iterator().next());
    }

    @Serdeable
    public record RequestDTO(String string) {

    }

    @Serdeable
    public record ResponseDTO(String string) {

    }

}