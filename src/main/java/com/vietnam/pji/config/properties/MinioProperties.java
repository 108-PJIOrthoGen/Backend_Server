package com.vietnam.pji.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "integration.minio", ignoreUnknownFields = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinioProperties {
    String accessKey;
    String secretKey;
    /** Internal endpoint used by the backend for put/get/delete (e.g. http://minio:9000). */
    String url;
    /** Public endpoint used when signing presigned URLs for browser consumption.
     *  Defaults to {@link #url} when unset (single-daemon dev setups). */
    String publicUrl;
    /** Default presigned URL validity in minutes. */
    Integer presignedExpiryMinutes = 60;
}
