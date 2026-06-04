-- Pending lab tasks now carry the render metadata emitted by the Rag_Agentic
-- completeness check (input_type / section / unit / normal_range). These let the
-- in-episode "Xét nghiệm chờ bổ sung" tab pick the right entry widget (lab
-- table vs clinical textarea vs culture form) and show the reference column
-- without a second lookup. All nullable — older rows simply render with sane
-- defaults.

ALTER TABLE "public"."pending_lab_tasks"
    ADD COLUMN "input_type" character varying(20),
    ADD COLUMN "section" character varying(20),
    ADD COLUMN "unit" character varying(40),
    ADD COLUMN "normal_range" character varying(60);

-- Persistent "reminders already saved" marker for an AI run, replacing the
-- previous localStorage-only flag. Lets the DataCompletenessStep "Lưu nhắc nhở"
-- button stay disabled across devices/sessions once a doctor has saved the
-- pending tasks for that run.
ALTER TABLE "public"."ai_recommendation_runs"
    ADD COLUMN "pending_tasks_saved" boolean DEFAULT false NOT NULL;
