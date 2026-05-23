package com.vietnam.pji.dto.response;

import java.util.Date;
import java.util.List;

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
public class RoleDetailDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private List<PermissionSummaryDTO> permissions;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}
