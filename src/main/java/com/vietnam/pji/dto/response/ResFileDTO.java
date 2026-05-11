package com.vietnam.pji.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResFileDTO {
    /** Legacy field — short-lived presigned URL for immediate display.
     *  Clients should persist `bucket` and `objectKey` and request a fresh URL on read. */
    private String fileName;
    private Instant timeUpload;
    /** Stable identifier of the uploaded object — persist this. */
    private String bucket;
    /** Stable identifier of the uploaded object — persist this. */
    private String objectKey;

    public ResFileDTO(String fileName, Instant timeUpload) {
        this.fileName = fileName;
        this.timeUpload = timeUpload;
    }
}
