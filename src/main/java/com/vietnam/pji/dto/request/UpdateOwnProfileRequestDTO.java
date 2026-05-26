package com.vietnam.pji.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Self-service profile-update payload. Intentionally excludes role, status,
 * and email — those remain admin-only edits via PUT /update-user. Password
 * change is optional; when {@code newPassword} is provided, {@code currentPassword}
 * must match the stored hash.
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

    private String currentPassword;

    @Size(min = 8, message = "Mật khẩu mới phải có ít nhất 8 ký tự")
    private String newPassword;
}
