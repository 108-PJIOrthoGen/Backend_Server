package com.vietnam.pji.exception;

import lombok.Getter;

/**
 * Thrown when a resource is currently locked by another user (HTTP 423 LOCKED).
 * Used by the episode soft-lock flow so the client can show which doctor is
 * editing and how long until the lock expires.
 */
@Getter
public class ResourceBusyException extends RuntimeException {

    private final Long heldBy;
    private final long ttlSeconds;

    public ResourceBusyException(String message, Long heldBy, long ttlSeconds) {
        super(message);
        this.heldBy = heldBy;
        this.ttlSeconds = ttlSeconds;
    }
}
