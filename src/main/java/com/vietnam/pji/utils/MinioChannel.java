package com.vietnam.pji.utils;

import com.vietnam.pji.config.properties.MinioProperties;
import com.vietnam.pji.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    /** Internal client for put/get/delete operations on the docker-network endpoint. */
    private final MinioClient minioClient;
    /** Presigner client signs URLs against the public endpoint so browsers can fetch them. */
    @Qualifier("minioPresignerClient")
    private final MinioClient minioPresignerClient;
    private final MinioProperties minioProperties;

    /** Result of an upload — clients should persist `bucket` and `objectKey`. */
    public record UploadResult(String bucket, String objectKey, String presignedUrl) {}

    public void initBucket(String bucket) {
        createBucket(bucket);
    }

    @SneakyThrows
    private void createBucket(final String name) {
        final var found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(name)
                        .build());
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build());

            // Public-read policy as a fallback for clients that don't follow presigned URLs.
            // Consider tightening to private + presigned-only for clinical data.
            final var policy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                           {
                              "Effect": "Allow",
                              "Principal": "*",
                              "Action": "s3:GetObject",
                              "Resource": "arn:aws:s3:::%s/*"
                            }
                          ]
                        }
                    """.formatted(name);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(name).config(policy).build());
        } else {
            log.info("Bucket %s đã tồn tại.".formatted(name));
        }
    }

    /**
     * Upload a file and return the stable identifier (bucket + objectKey) plus a short-lived
     * presigned URL for immediate display. Callers MUST persist bucket+objectKey, not the URL.
     */
    @SneakyThrows
    public UploadResult uploadObject(@NonNull final MultipartFile file, String bucket) {
        log.info("Bucket: {}, file size: {}", bucket, file.getSize());
        final var objectKey = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .contentType(Objects.isNull(file.getContentType()) ? "image/png; image/jpg;"
                                    : file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
        } catch (Exception ex) {
            log.error("Error saving image \n {} ", ex.getMessage());
            throw new BusinessException("400", "Unable to upload file", ex);
        }
        return new UploadResult(bucket, objectKey, presignedGetUrl(bucket, objectKey));
    }

    /**
     * Legacy entry point — returns just the presigned URL.
     * @deprecated use {@link #uploadObject(MultipartFile, String)} so callers can persist
     *             bucket+objectKey and regenerate presigned URLs on read.
     */
    @Deprecated
    public String upload(@NonNull final MultipartFile file, String bucket) {
        return uploadObject(file, bucket).presignedUrl();
    }

    /**
     * Generate a fresh presigned GET URL for a stored object. Use this on every read so URLs
     * never expire from the caller's perspective.
     */
    @SneakyThrows
    public String presignedGetUrl(String bucket, String objectKey) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            return null;
        }
        int expiryMinutes = minioProperties.getPresignedExpiryMinutes() != null
                ? minioProperties.getPresignedExpiryMinutes() : 60;
        return minioPresignerClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectKey)
                        .expiry(expiryMinutes, TimeUnit.MINUTES)
                        .build());
    }

    public byte[] download(String bucket, String name) {
        try (GetObjectResponse inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(name)
                .build())) {
            String contentLength = inputStream.headers().get(HttpHeaders.CONTENT_LENGTH);
            int size = StringUtils.isEmpty(contentLength) ? 0 : Integer.parseInt(contentLength);
            return ConverterUtils.readBytesFromInputStream(inputStream, size);
        } catch (Exception e) {
            throw new BusinessException("400", "Unable to download file", e);
        }
    }
}
