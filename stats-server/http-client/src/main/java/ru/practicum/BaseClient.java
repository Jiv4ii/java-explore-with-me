package ru.practicum;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {

    private final RestTemplate rest;

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(T body) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", null, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod httpMethod,
                                                          String path, @Nullable Map<String, Object> parameters, @Nullable T body) {

        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHttpHeaders());
        ResponseEntity<Object> exploreResponseEntity;

        try {
            if (parameters != null) {
                exploreResponseEntity = rest.exchange(path, httpMethod, requestEntity, Object.class, parameters);
            } else {
                exploreResponseEntity = rest.exchange(path, httpMethod, requestEntity, Object.class);

            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareExploreResponse(exploreResponseEntity);
    }

    private HttpHeaders defaultHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareExploreResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}