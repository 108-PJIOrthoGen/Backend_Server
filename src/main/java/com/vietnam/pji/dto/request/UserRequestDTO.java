package com.vietnam.pji.dto.request;

import com.vietnam.pji.constant.UserStatus;
import com.vietnam.pji.model.auth.Role;
import com.vietnam.pji.utils.validators.EnumPattern;
import com.vietnam.pji.utils.validators.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private Long id;

    @NotBlank(message = "fullName must be not blank")
    private String fullName;

    @NotBlank(message = "department must be not blank")
    private String department;

    @Email(message = "email invalid format")
    private String email;

    @PhoneNumber(message = "phone invalid format")
    private String phone;

    private String avatar;

    private String password;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    private UserStatus status;

    private Role role;

}
