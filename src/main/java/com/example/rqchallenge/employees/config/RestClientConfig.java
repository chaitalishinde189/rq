package com.example.rqchallenge.employees.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author chaitali shinde
 * Configuration for rest template
 */
@Configuration
public class RestClientConfig {

    @Value("${rest.template.connect.timeout: 30000}")
    private int connectTimeout;

    @Value("${rest.template.read.timeout: 30000}")
    private int readTimeout;

    @Bean
    @Primary
    public RestTemplate getRestTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return new BufferingClientHttpRequestFactory(clientHttpRequestFactory);
    }

}
