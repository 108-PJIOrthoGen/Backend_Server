package com.vietnam.pji.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Error payload for HTTP 423 LOCKED. Extends {@link ErrorResponse} with the
 * current lock holder and remaining TTL so the client can show "Doctor X is
 * editing this for another N seconds".
 */
@Getter
@Setter
public class LockedErrorResponse extends ErrorResponse {
    private Long heldBy;
    private long ttlSeconds;
}
