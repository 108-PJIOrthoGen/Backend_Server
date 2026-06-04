package com.vietnam.pji.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Self-service profile-update payload. Intentionally excludes role, status,
 * and email — those remain admin-only edits via PUT /update-user. Password
 * changes are handled separately by POST /auth/change-password
 * ({@link ChangePasswordRequestDTO}) so they can require the current
 * password and revoke existing sessions.
 */
@Getter
@Setter
public class UpdateOwnProfileRequestDTO {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Pattern(regexp = "^(\\+?\\d{1,3})?[\\s.-]?\\d{9,11}$|^$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String department;

    private String avatar;
}
