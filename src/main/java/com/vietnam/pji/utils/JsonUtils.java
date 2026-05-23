package com.vietnam.pji.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    /**
     * Best-effort parse of a JSON string into a {@code Map<String, Object>}. Returns
     * {@code null} for blank input. On parse failure the raw string is returned so
     * downstream consumers can still surface the original value rather than dropping it.
     */
    public static Object safeReadMap(ObjectMapper mapper, String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return mapper.readValue(raw, MAP_TYPE);
        } catch (Exception e) {
            return raw;
        }
    }
}
