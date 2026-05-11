package com.vietnam.pji.config.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vietnam.pji.config.properties.MinioProperties;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    /** Internal client — used for put/get/delete on the docker network endpoint. */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    /** Presigner client — signs URLs against the public endpoint so browsers can fetch them.
     *  Falls back to the internal client if no public URL is configured. */
    @Bean(name = "minioPresignerClient")
    public MinioClient minioPresignerClient() {
        String publicUrl = minioProperties.getPublicUrl();
        if (!StringUtils.hasText(publicUrl) || publicUrl.equals(minioProperties.getUrl())) {
            return minioClient();
        }
        return MinioClient.builder()
                .endpoint(publicUrl)
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
