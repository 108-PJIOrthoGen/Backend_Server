package com.vietnam.pji.config.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ExtractImagesClientConfig {

    @Value("${extract-images.base-url:http://localhost:8002}")
    private String baseUrl;

    @Value("${extract-images.connect-timeout:3000}")
    private long connectTimeoutMs;

    @Value("${extract-images.read-timeout:30000}")
    private long readTimeoutMs;

    @Bean(name = "extractImagesRestTemplate")
    public RestTemplate extractImagesRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(baseUrl)
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}
