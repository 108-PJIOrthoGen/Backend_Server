-- The episode aggregate save can create a culture row before a result is known,
-- so allow result_status to be null. The existing DEFAULT 'PENDING' still applies
-- when the column is omitted from an INSERT; this only relaxes the explicit-null case.
ALTER TABLE "public"."culture_results"
    ALTER COLUMN "result_status" DROP NOT NULL;
