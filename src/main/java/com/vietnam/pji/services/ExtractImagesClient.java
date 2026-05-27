package com.vietnam.pji.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class ExtractImagesClient {

    private final RestTemplate extractImagesRestTemplate;

    public ExtractImagesClient(@Qualifier("extractImagesRestTemplate") RestTemplate extractImagesRestTemplate) {
        this.extractImagesRestTemplate = extractImagesRestTemplate;
    }

    public Map<String, Object> upload(MultipartFile[] files) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("files", resource);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = extractImagesRestTemplate.exchange(
                "/upload", HttpMethod.POST, request, Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> data = response.getBody();
        return data;
    }

    public Map<String, Object> getResult(String jobId) {
        try {
            ResponseEntity<Map> response = extractImagesRestTemplate.getForEntity(
                    "/result/" + jobId, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> data = response.getBody();
            return data;
        } catch (HttpStatusCodeException e) {
            log.warn("Extract_Images returned {} for job {}", e.getStatusCode(), jobId);
            throw e;
        }
    }

    /**
     * Cancel a job upstream: the extract service marks it cancelled and deletes
     * its uploaded files + any result. Idempotent on the upstream side.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> cancel(String jobId) {
        try {
            ResponseEntity<Map> response = extractImagesRestTemplate.exchange(
                    "/jobs/" + jobId, HttpMethod.DELETE, null, Map.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.warn("Extract_Images cancel returned {} for job {}", e.getStatusCode(), jobId);
            throw e;
        }
    }
}
