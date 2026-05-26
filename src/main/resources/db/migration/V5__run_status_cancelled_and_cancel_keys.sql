-- V5__run_status_cancelled_and_cancel_keys.sql
-- Adds CANCELLED to the ai_run_status Postgres enum so the @Enumerated(STRING)
-- column on ai_recommendation_runs.status can accept it.

ALTER TYPE "public"."ai_run_status" ADD VALUE IF NOT EXISTS 'CANCELLED';
