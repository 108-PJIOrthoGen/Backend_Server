package com.vietnam.pji.services;

public interface DeviceVerificationService {

    /**
     * Snapshot of a pending new-device challenge. The candidate device id is
     * promoted to a trusted device only after the user proves possession of
     * the email by submitting the matching OTP.
     */
    record Challenge(String email, String candidateDeviceId, String userAgent, String ipAddress) {}

    /**
     * Generate an OTP for this email/device, persist the challenge in Redis,
     * and email the code. Returns the challengeId the client must echo back
     * when calling {@link #verifyChallenge}.
     */
    String startChallenge(String email, String candidateDeviceId, String userAgent, String ipAddress);

    /**
     * Validate the OTP for the given challenge. On success, returns the
     * challenge snapshot and clears the OTP from Redis. On failure increments
     * the attempt counter and throws InvalidDataException (HTTP 409).
     */
    Challenge verifyChallenge(String email, String challengeId, String otp);

    /** Number of days a verified device stays trusted. Used for cookie + DB expiry. */
    int trustedDeviceDays();
}
