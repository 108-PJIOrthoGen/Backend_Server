package com.vietnam.pji.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionSummaryDTO {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
}
