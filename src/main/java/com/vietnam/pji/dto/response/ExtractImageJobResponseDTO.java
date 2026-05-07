package com.vietnam.pji.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractImageJobResponseDTO {
    private String jobId;
    private String status;
    private Integer fileCount;
    private Map<String, Object> extracted;
    private String error;
}
