package com.vietnam.pji.config.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AiServiceClientConfig {

    @Value("${ai.service.base-url:http://localhost:8000}")
    private String aiServiceBaseUrl;

    @Value("${ai.service.connect-timeout:3000}")
    private long connectTimeoutMs;

    @Value("${ai.service.read-timeout:60000}")
    private long readTimeoutMs;

    @Value("${ai.service.api-key:}")
    private String aiServiceApiKey;

    @Bean(name = "aiRestTemplate")
    public RestTemplate aiRestTemplate(RestTemplateBuilder builder) {
        RestTemplateBuilder configured = builder
                .rootUri(aiServiceBaseUrl)
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs));

        if (aiServiceApiKey != null && !aiServiceApiKey.isBlank()) {
            configured = configured.additionalInterceptors((request, body, execution) -> {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + aiServiceApiKey);
                return execution.execute(request, body);
            });
        }

        return configured.build();
    }
}
