-- Tracks browsers/devices the user has verified via email OTP.
-- Presence of a non-expired row lets the matching device skip the OTP
-- challenge on subsequent logins; absence forces a new-device verification.

CREATE TABLE "public"."user_trusted_devices" (
    "id"            bigserial PRIMARY KEY,
    "user_id"       bigint NOT NULL,
    "device_id"     varchar(64) NOT NULL,
    "device_label"  varchar(255),
    "ip_address"    varchar(64),
    "last_seen_at"  timestamp with time zone NOT NULL,
    "expires_at"    timestamp with time zone NOT NULL,
    "created_at"    timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "fk_trusted_device_user"
        FOREIGN KEY ("user_id") REFERENCES "public"."users"("id") ON DELETE CASCADE,
    CONSTRAINT "uk_user_device" UNIQUE ("user_id", "device_id")
);

CREATE INDEX "idx_trusted_device_expires"
    ON "public"."user_trusted_devices" ("expires_at");
