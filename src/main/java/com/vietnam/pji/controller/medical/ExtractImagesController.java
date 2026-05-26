package com.vietnam.pji.controller.medical;

import com.vietnam.pji.dto.response.ExtractImageJobResponseDTO;
import com.vietnam.pji.dto.response.ResponseData;
import com.vietnam.pji.services.ExtractImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/extract-images")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Extract Images", description = "Submit medical record images for VLM-based structured data extraction")
public class ExtractImagesController {

    private static final int MAX_FILES = 10;

    private final ExtractImagesService extractImagesService;

    @PostMapping("/jobs")
    @Operation(summary = "Create an extract-images job from uploaded medical record images")
    public ResponseData<ExtractImageJobResponseDTO> createJob(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "episodeId", required = false) Long episodeId) throws IOException {
        if (files == null || files.length == 0) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "No files provided", null);
        }
        if (files.length > MAX_FILES) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(),
                    "Too many files (max " + MAX_FILES + ")", null);
        }
        ExtractImageJobResponseDTO data = extractImagesService.createJob(files, episodeId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Extraction job queued", data);
    }

    @GetMapping("/jobs/{jobId}")
    @Operation(summary = "Get extract-images job status and result")
    public ResponseData<ExtractImageJobResponseDTO> getJob(@PathVariable String jobId) {
        ExtractImageJobResponseDTO data = extractImagesService.getJob(jobId);
        String status = data.getStatus();
        if ("completed".equals(status)) {
            return new ResponseData<>(HttpStatus.OK.value(), "Extraction completed", data);
        }
        if ("failed".equals(status)) {
            return new ResponseData<>(HttpStatus.OK.value(), "Extraction failed", data);
        }
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Extraction job is processing", data);
    }
}
