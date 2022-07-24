package com.example.rqchallenge.employees.util;

import com.example.rqchallenge.employees.annotation.LogExecutionTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author chaitali shinde
 * Rest client using rest template which retries when received error 'Too many requests' from api
 */
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RetryableRestClient {

    private final RestTemplate restTemplate;

    /**
     * Executes HTTP method for mentioned url with request entity.
     * Retries on HttpClientErrorException.TooManyRequests as per retry configuration.
     *
     * @param url
     * @param method
     * @param requestEntity
     * @param responseType
     * @param <T>
     * @return responseEntity
     */
    @Retryable(value = {HttpClientErrorException.TooManyRequests.class},
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}"))
    @LogExecutionTime
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
                                          Class<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }
}
