-- The episode aggregate save (/episodes/full) always upserts a clinical record,
-- even when the clinical exam tab has not been filled in yet (a brand-new episode
-- at admission time). suspected_infection_type and implant_stability were NOT NULL,
-- so that insert failed with a not-null violation surfaced to the client as a
-- generic 400 "Invalid Data". Relax both columns; they get filled in once the
-- clinical examination is actually recorded.
ALTER TABLE "public"."clinical_records"
    ALTER COLUMN "suspected_infection_type" DROP NOT NULL;

ALTER TABLE "public"."clinical_records"
    ALTER COLUMN "implant_stability" DROP NOT NULL;
