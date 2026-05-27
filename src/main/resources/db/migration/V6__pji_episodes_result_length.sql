-- V6__pji_episodes_result_length.sql
-- Widen pji_episodes.result from varchar(30) to varchar(200); the previous
-- limit was too small and rejected longer result values with
-- "value too long for type character varying(30)". NOT NULL is preserved.

ALTER TABLE "public"."pji_episodes"
    ALTER COLUMN "result" TYPE character varying(200);
