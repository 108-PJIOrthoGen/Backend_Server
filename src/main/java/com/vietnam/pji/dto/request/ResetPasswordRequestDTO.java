package com.vietnam.pji.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {
    @NotBlank(message = "email không được để trống")
    @Email(message = "email không hợp lệ")
    private String email;

    @NotBlank(message = "mã OTP không được để trống")
    @Pattern(regexp = "\\d{6}", message = "mã OTP phải gồm 6 chữ số")
    private String otp;

    @NotBlank(message = "mật khẩu mới không được để trống")
    @Size(min = 8, message = "mật khẩu mới phải có ít nhất 8 ký tự")
    private String newPassword;
}
