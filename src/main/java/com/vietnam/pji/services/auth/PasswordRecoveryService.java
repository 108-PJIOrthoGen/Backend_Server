package com.vietnam.pji.services.auth;

public interface PasswordRecoveryService {
    void requestOtp(String email);

    void resetPassword(String email, String otp, String newPassword);
}
