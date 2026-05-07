package com.vietnam.pji.services;

public interface PasswordRecoveryService {
    void requestOtp(String email);

    void resetPassword(String email, String otp, String newPassword);
}
