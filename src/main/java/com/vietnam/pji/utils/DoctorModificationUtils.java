package com.vietnam.pji.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Helpers for resolving doctor modifications stored on
 * {@code DoctorRecommendationReview.modificationJson}. The map is free-form: doctors
 * may key edits by recommendation category (e.g. {@code "DIAGNOSTIC_TEST"}) or by
 * item id (raw or prefixed with {@code item_}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoctorModificationUtils {

    /**
     * Look up the doctor's override for a given item.
     *
     * @return the doctor-provided value, or {@code null} when no override applies
     */
    public static Object extractOverride(Map<String, Object> modificationJson, String categoryKey, Long itemId) {
        if (modificationJson == null || modificationJson.isEmpty()) {
            return null;
        }
        if (categoryKey != null && modificationJson.containsKey(categoryKey)) {
            return modificationJson.get(categoryKey);
        }
        if (itemId != null) {
            String prefixed = "item_" + itemId;
            if (modificationJson.containsKey(prefixed)) {
                return modificationJson.get(prefixed);
            }
            String idKey = String.valueOf(itemId);
            if (modificationJson.containsKey(idKey)) {
                return modificationJson.get(idKey);
            }
        }
        return null;
    }
}
