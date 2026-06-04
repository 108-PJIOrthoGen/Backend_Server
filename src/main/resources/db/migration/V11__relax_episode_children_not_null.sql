-- Continue the V10 cleanup: the episode aggregate save (/episodes/full) writes
-- every child table in one transaction, so any NOT NULL business column a user
-- can leave blank surfaces as a generic 400 "Invalid Data". Relax the remaining
-- ones. FK columns, ids and audit timestamps stay NOT NULL.

-- A sensitivity row may be drafted before the antibiotic name is decided.
ALTER TABLE "public"."sensitivity_results"
    ALTER COLUMN "antibiotic_name" DROP NOT NULL;

-- A surgery row may be drafted before its date/type are known.
ALTER TABLE "public"."surgeries"
    ALTER COLUMN "surgery_date" DROP NOT NULL;

ALTER TABLE "public"."surgeries"
    ALTER COLUMN "surgery_type" DROP NOT NULL;

-- Treatment outcome is unknown at admission time; the create form does not
-- require it. This was the first not-null violation reported on /episodes/full.
ALTER TABLE "public"."pji_episodes"
    ALTER COLUMN "result" DROP NOT NULL;
