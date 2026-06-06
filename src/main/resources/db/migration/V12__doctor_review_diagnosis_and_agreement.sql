-- The "Chẩn đoán bác sĩ" step now captures the doctor's own final diagnosis
-- alongside their treatment-plan edits (modification_json):
--   * doctor_diagnosis_json — {pji_conclusion, infection_classification,
--     primary_diagnosis, clinical_reasoning, identified_organism}
--   * agreement_json — per-criterion AI-vs-doctor agreement booleans plus an
--     overall agreement_rate (0-100), computed at save time. Powers the
--     compare-result table, the consensus statistics, and future AI learning
--     from the doctor's final word.

ALTER TABLE "public"."doctor_recommendation_reviews"
    ADD COLUMN "doctor_diagnosis_json" jsonb,
    ADD COLUMN "agreement_json" jsonb;
