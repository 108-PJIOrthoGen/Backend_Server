package com.vietnam.pji.controller.medical;

import com.vietnam.pji.dto.response.ResFileDTO;
import com.vietnam.pji.exception.BusinessException;
import com.vietnam.pji.utils.MinioChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/files")
@Tag(name = "Files (MinIO)", description = "Upload medical files (PDF/JPEG/PNG/WEBP/JPG/DCM) to MinIO storage")
public class MinioController {
    private final MinioChannel minioChannel;

    @Operation(summary = "Upload file", description = "Stores a file in the given MinIO folder and returns a presigned URL")
    @PostMapping("")
    public ResponseEntity<ResFileDTO> uploadData(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, BusinessException {
        // CHECK VALIDATE
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Not leave blank, Please upload!!");
        }
        String fileName = file.getOriginalFilename();
        List<String> extensionsAllowed = Arrays.asList("pdf", "jpeg", "png", "webp", "jpg", "dcm");
        boolean isValid = extensionsAllowed.stream().anyMatch(item -> {
            assert fileName != null;
            return fileName.toLowerCase().endsWith(item);
        });
        if (!isValid) {
            throw new BusinessException("Invalid file format, Please try again!");
        }
        // handle create folder (Option)
        this.minioChannel.initBucket(folder);
        var uploaded = this.minioChannel.uploadObject(file, folder);
        ResFileDTO result = new ResFileDTO(
                uploaded.presignedUrl(),
                Instant.now(),
                uploaded.bucket(),
                uploaded.objectKey());

        return ResponseEntity.ok().body(result);
    }
}
