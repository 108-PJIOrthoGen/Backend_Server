package com.vietnam.pji.utils.mapper;

import java.util.List;

import com.vietnam.pji.dto.response.PermissionSummaryDTO;
import com.vietnam.pji.dto.response.RoleDetailDTO;
import com.vietnam.pji.model.auth.Permission;
import com.vietnam.pji.model.auth.Role;
import org.mapstruct.Mapper;

@Mapper(config = DefaultConfigMapper.class)
public interface RoleMapper {

    RoleDetailDTO toDetail(Role role);

    List<RoleDetailDTO> toDetails(List<Role> roles);

    PermissionSummaryDTO toPermissionSummary(Permission permission);

    List<PermissionSummaryDTO> toPermissionSummaries(List<Permission> permissions);
}
