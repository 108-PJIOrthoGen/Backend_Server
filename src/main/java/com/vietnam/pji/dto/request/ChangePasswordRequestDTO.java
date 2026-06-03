package com.vietnam.pji.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Self-service password change payload for POST /auth/change-password.
 * Both fields are mandatory — unlike profile updates, a password change
 * always re-authenticates against the stored hash. On success, the user's
 * refresh token is revoked so every session must log in again.
 */
@Getter
@Setter
public class ChangePasswordRequestDTO {

    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, message = "Mật khẩu mới phải có ít nhất 8 ký tự")
    private String newPassword;
}
