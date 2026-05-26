package com.vietnam.pji.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyDeviceRequestDTO {
    @NotBlank(message = "email không được để trống")
    @Email(message = "email không hợp lệ")
    private String email;

    @NotBlank(message = "challengeId không được để trống")
    private String challengeId;

    @NotBlank(message = "mã OTP không được để trống")
    @Pattern(regexp = "\\d{6}", message = "mã OTP phải gồm 6 chữ số")
    private String otp;
}
