-- Add height/weight to clinical records so each episode can persist the raw
-- anthropometric inputs used to derive BMI (previously only BMI was stored).
ALTER TABLE "public"."clinical_records"
    ADD COLUMN "height_cm" numeric(5,2),
    ADD COLUMN "weight_kg" numeric(5,2);
