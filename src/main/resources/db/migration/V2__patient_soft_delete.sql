-- Soft-delete support for patients.
-- Rows stay in the DB so that FKs from episodes, pending_lab_tasks,
-- and clinical history don't break; they're hidden from JPA queries by
-- @SQLRestriction("deleted_at IS NULL") on the Patient entity.

ALTER TABLE "public"."patients"
    ADD COLUMN "deleted_at" timestamp without time zone;

CREATE INDEX "idx_patients_deleted_at"
    ON "public"."patients" ("deleted_at");
