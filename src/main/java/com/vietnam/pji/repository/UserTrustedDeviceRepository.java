package com.vietnam.pji.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vietnam.pji.model.auth.UserTrustedDevice;

public interface UserTrustedDeviceRepository extends JpaRepository<UserTrustedDevice, Long> {

    Optional<UserTrustedDevice> findByUserIdAndDeviceIdAndExpiresAtAfter(Long userId, String deviceId, Instant now);

    Optional<UserTrustedDevice> findByUserIdAndDeviceId(Long userId, String deviceId);

    void deleteByUserId(Long userId);
}
