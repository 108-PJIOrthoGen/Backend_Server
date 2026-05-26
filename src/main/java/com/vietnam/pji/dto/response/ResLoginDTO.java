package com.vietnam.pji.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    @JsonProperty("access_token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserData user;

    // ===== Fields used when the login was blocked pending email OTP verification =====
    // Present only on the response from POST /auth/login when the request came
    // from a new (untrusted) device. The client must then POST /auth/verify-device
    // with the same email + challengeId + OTP to receive tokens.

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean requiresDeviceVerification;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String challengeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String maskedEmail;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private long id;
        private String name;
        private String email;
        private RoleDetailDTO role;

        // Optional profile fields — populated by GET/PUT /auth/account so the
        // self-service settings modal can pre-fill. Omitted from login/refresh
        // responses where they would be redundant noise.
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String phone;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String department;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String avatar;

        /** Compact constructor for login/refresh paths that don't carry the optional profile fields. */
        public UserData(long id, String name, String email, RoleDetailDTO role) {
            this(id, name, email, role, null, null, null);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAccountUser {
        private UserData user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoWithinToken {
        private long id;
        private String email;
        private String name;
    }
}
