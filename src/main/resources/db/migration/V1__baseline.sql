--
-- PostgreSQL database dump
--

-- Dumped from database version 16.13
-- Dumped by pg_dump version 16.13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ai_run_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."ai_run_status" AS ENUM (
    'SUCCESS',
    'FAILED',
    'PARTIAL',
    'QUEUED',
    'PROCESSING',
    'TIMEOUT'
);


--
-- Name: ai_run_trigger_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."ai_run_trigger_type" AS ENUM (
    'MANUAL_GENERATE',
    'AUTO_REFRESH',
    'DATA_CHANGED',
    'DOCTOR_REQUEST',
    'REVIEW_REQUEST'
);


--
-- Name: culturestatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."culturestatus" AS ENUM (
    'CONTAMINATED',
    'FINAL_NEGATIVE',
    'NOT_PERFORMED',
    'NO_GROWTH',
    'PENDING',
    'POSITIVE'
);


--
-- Name: direct_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."direct_enum" AS ENUM (
    'CC',
    'KKB',
    'KDT'
);


--
-- Name: gender_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."gender_type" AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);


--
-- Name: genderenum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."genderenum" AS ENUM (
    'FEMALE',
    'MALE',
    'OTHER'
);


--
-- Name: implant_stability_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."implant_stability_type" AS ENUM (
    'STABLE',
    'POSSIBLY_LOOSE',
    'LOOSE',
    'UNKNOWN'
);


--
-- Name: implanttype; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."implanttype" AS ENUM (
    'LOOSE',
    'POSSIBLY_LOOSE',
    'STABLE',
    'UNKNOWN'
);


--
-- Name: infection_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."infection_type" AS ENUM (
    'EARLY_POSTOPERATIVE',
    'DELAYED',
    'LATE_HEMATOGENOUS',
    'ACUTE_HEMATOGENOUS',
    'CHRONIC',
    'UNKNOWN'
);


--
-- Name: infectiontype; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."infectiontype" AS ENUM (
    'ACUTE_HEMATOGENOUS',
    'CHRONIC',
    'DELAYED',
    'EARLY_POSTOPERATIVE',
    'LATE_HEMATOGENOUS',
    'UNKNOWN'
);


--
-- Name: recommendation_item_category; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."recommendation_item_category" AS ENUM (
    'DIAGNOSTIC_TEST',
    'SYSTEMIC_ANTIBIOTIC',
    'LOCAL_ANTIBIOTIC',
    'SURGERY_PROCEDURE'
);


--
-- Name: review_status_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."review_status_type" AS ENUM (
    'ACCEPTED',
    'MODIFIED',
    'REJECTED',
    'SAVED_DRAFT'
);


--
-- Name: sample_result_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."sample_result_status" AS ENUM (
    'NOT_PERFORMED',
    'PENDING',
    'NO_GROWTH',
    'POSITIVE',
    'CONTAMINATED',
    'FINAL_NEGATIVE'
);


--
-- Name: treatment_plan_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."treatment_plan_status" AS ENUM (
    'DRAFT',
    'CONFIRMED',
    'SUPERSEDED',
    'CANCELLED'
);


--
-- Name: userstatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE "public"."userstatus" AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'NONE'
);


--
-- Name: CAST ("public"."culturestatus" AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST ("public"."culturestatus" AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST ("public"."genderenum" AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST ("public"."genderenum" AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST ("public"."implanttype" AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST ("public"."implanttype" AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST ("public"."infectiontype" AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST ("public"."infectiontype" AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST ("public"."userstatus" AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST ("public"."userstatus" AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS "public"."culturestatus"); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS "public"."culturestatus") WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS "public"."genderenum"); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS "public"."genderenum") WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS "public"."implanttype"); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS "public"."implanttype") WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS "public"."infectiontype"); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS "public"."infectiontype") WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS "public"."userstatus"); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS "public"."userstatus") WITH INOUT AS IMPLICIT;


SET default_tablespace = '';

SET default_table_access_method = "heap";

--
-- Name: ai_chat_messages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."ai_chat_messages" (
    "id" bigint NOT NULL,
    "session_id" bigint,
    "role" character varying(20),
    "content" "text" NOT NULL,
    "tokens_used" integer,
    "context_json" "jsonb",
    "latency_ms" bigint,
    "references_json" "jsonb",
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255)
);


--
-- Name: ai_chat_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."ai_chat_messages_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ai_chat_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."ai_chat_messages_id_seq" OWNED BY "public"."ai_chat_messages"."id";


--
-- Name: ai_chat_sessions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."ai_chat_sessions" (
    "id" bigint NOT NULL,
    "run_id" bigint,
    "current_item_id" bigint,
    "chat_type" character varying(30),
    "episode_id" bigint,
    "title" character varying(500),
    "is_archived" boolean DEFAULT false,
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255)
);


--
-- Name: ai_chat_sessions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."ai_chat_sessions_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ai_chat_sessions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."ai_chat_sessions_id_seq" OWNED BY "public"."ai_chat_sessions"."id";


--
-- Name: ai_rag_citations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."ai_rag_citations" (
    "id" bigint NOT NULL,
    "run_id" bigint NOT NULL,
    "item_id" bigint,
    "source_type" character varying(30),
    "source_title" character varying(500),
    "source_uri" character varying(1000),
    "snippet" "text",
    "relevance_score" numeric(5,4),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "cited_for" character varying(500),
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255)
);


--
-- Name: ai_rag_citations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."ai_rag_citations" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."ai_rag_citations_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: ai_recommendation_items; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."ai_recommendation_items" (
    "id" bigint NOT NULL,
    "run_id" bigint NOT NULL,
    "category" character varying(30) NOT NULL,
    "title" character varying(500) NOT NULL,
    "priority_order" integer DEFAULT 1 NOT NULL,
    "is_primary" boolean DEFAULT false NOT NULL,
    "item_json" "jsonb" NOT NULL,
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255)
);


--
-- Name: ai_recommendation_items_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."ai_recommendation_items" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."ai_recommendation_items_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: ai_recommendation_runs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."ai_recommendation_runs" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "snapshot_id" bigint NOT NULL,
    "run_no" integer NOT NULL,
    "trigger_type" character varying(30) NOT NULL,
    "status" character varying(20) DEFAULT 'SUCCESS'::"public"."ai_run_status" NOT NULL,
    "model_name" character varying(100),
    "model_version" character varying(50),
    "assessment_json" "jsonb",
    "explanation_json" "jsonb",
    "warnings_json" "jsonb",
    "latency_ms" bigint,
    "error_message" "text",
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "request_id" character varying(255),
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255),
    "data_completeness_json" "jsonb"
);


--
-- Name: ai_recommendation_runs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."ai_recommendation_runs" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."ai_recommendation_runs_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: case_clinical_snapshots; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."case_clinical_snapshots" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "snapshot_no" integer NOT NULL,
    "snapshot_data_json" "jsonb" NOT NULL,
    "data_completeness_score" numeric(5,2),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_by" character varying(255),
    "updated_at" timestamp without time zone,
    "created_by" character varying(255)
);


--
-- Name: case_clinical_snapshots_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."case_clinical_snapshots" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."case_clinical_snapshots_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: clinical_records; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."clinical_records" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "recorded_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "illness_onset_date" "date",
    "blood_pressure" character varying(20),
    "bmi" numeric(4,2),
    "fever" boolean,
    "pain" boolean,
    "erythema" boolean,
    "swelling" boolean,
    "sinus_tract" boolean,
    "suspected_infection_type" "public"."infection_type" NOT NULL,
    "hematogenous_suspected" boolean,
    "implant_stability" character varying(50) NOT NULL,
    "soft_tissue" character varying(255),
    "pmma_allergy" boolean,
    "prosthesis_joint" character varying(255),
    "days_since_index_arthroplasty" integer,
    "notations" "text",
    "created_by" character varying(255) NOT NULL,
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: clinical_records_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."clinical_records_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: clinical_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."clinical_records_id_seq" OWNED BY "public"."clinical_records"."id";


--
-- Name: culture_results; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."culture_results" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "sample_type" character varying(100),
    "incubation_days" integer,
    "name" character varying(255),
    "result_status" "public"."sample_result_status" DEFAULT 'PENDING'::"public"."sample_result_status" NOT NULL,
    "gram_type" character varying(20),
    "notes" "text",
    "created_by" character varying(255),
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "days_off_antibio" integer,
    "antibioticed" boolean
);


--
-- Name: culture_results_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."culture_results_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: culture_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."culture_results_id_seq" OWNED BY "public"."culture_results"."id";


--
-- Name: doctor_recommendation_reviews; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."doctor_recommendation_reviews" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "run_id" bigint NOT NULL,
    "review_status" character varying(20) NOT NULL,
    "review_note" "text",
    "modification_json" "jsonb",
    "rejection_reason" "text",
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_by" character varying(255),
    "created_by" character varying(255)
);


--
-- Name: doctor_recommendation_reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."doctor_recommendation_reviews" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."doctor_recommendation_reviews_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: image_results; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."image_results" (
    "id" bigint NOT NULL,
    "episode_id" bigint,
    "type" character varying(50),
    "findings" "text",
    "file_metadata" "jsonb",
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "created_by" bigint,
    "updated_by" bigint,
    "bucket" character varying(200),
    "object_key" character varying(500)
);


--
-- Name: image_results_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."image_results_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: image_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."image_results_id_seq" OWNED BY "public"."image_results"."id";


--
-- Name: lab_results; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."lab_results" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "biochemical_data" "jsonb",
    "created_by" character varying(255),
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "leu" "jsonb",
    "fluid_analysis" "jsonb",
    "hematology_tests" "jsonb"
);


--
-- Name: lab_results_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."lab_results_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lab_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."lab_results_id_seq" OWNED BY "public"."lab_results"."id";


--
-- Name: medical_histories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."medical_histories" (
    "episode_id" bigint NOT NULL,
    "medical_history" "text",
    "process" "text",
    "antibiotic_history" "text",
    "is_allergy" boolean,
    "allergy_note" character varying(255),
    "is_drug" boolean,
    "drug_note" character varying(255),
    "is_alcohol" boolean,
    "alcohol_note" character varying(255),
    "is_smoking" boolean,
    "smoking_note" character varying(255),
    "is_other" boolean,
    "other_note" character varying(255),
    "created_by" character varying(255) NOT NULL,
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: patients; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."patients" (
    "id" bigint NOT NULL,
    "patient_code" character varying(30),
    "full_name" character varying(100) NOT NULL,
    "date_of_birth" "date" NOT NULL,
    "gender" "public"."gender_type" NOT NULL,
    "identity_card" character varying(50),
    "insurance_number" character varying(50),
    "insurance_expired" "date",
    "nationality" character varying(50),
    "ethnicity" character varying(50),
    "phone" character varying(20),
    "career" character varying(50),
    "subject" character varying(50),
    "address" "text",
    "relative_info" "jsonb",
    "created_by" character varying(255) NOT NULL,
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: patients_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."patients_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: patients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."patients_id_seq" OWNED BY "public"."patients"."id";


--
-- Name: pending_lab_tasks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."pending_lab_tasks" (
    "id" bigint NOT NULL,
    "created_at" timestamp(6) without time zone,
    "created_by" character varying(255),
    "updated_at" timestamp(6) without time zone,
    "updated_by" character varying(255),
    "assigned_to_user_id" bigint,
    "category" character varying(30) NOT NULL,
    "created_from_run_id" bigint,
    "field" character varying(80) NOT NULL,
    "importance" character varying(20) NOT NULL,
    "message" "text",
    "status" character varying(20) NOT NULL,
    "episode_id" bigint NOT NULL,
    "fulfilled_lab_result_id" bigint,
    "patient_id" bigint NOT NULL,
    CONSTRAINT "pending_lab_tasks_status_check" CHECK ((("status")::"text" = ANY (ARRAY[('PENDING'::character varying)::"text", ('FULFILLED'::character varying)::"text", ('DISMISSED'::character varying)::"text"])))
);


--
-- Name: pending_lab_tasks_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE "public"."pending_lab_tasks" ALTER COLUMN "id" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME "public"."pending_lab_tasks_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."permissions" (
    "id" bigint NOT NULL,
    "api_path" character varying(255) NOT NULL,
    "method" character varying(255),
    "module" character varying(255),
    "name" character varying(255),
    "created_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "updated_by" character varying(255),
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."permissions_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."permissions_id_seq" OWNED BY "public"."permissions"."id";


--
-- Name: pji_episodes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."pji_episodes" (
    "id" bigint NOT NULL,
    "patient_id" bigint NOT NULL,
    "admission_date" "date" NOT NULL,
    "discharge_date" "date",
    "department" character varying(255),
    "treatment_days" integer,
    "direct" character varying(255),
    "reason" "text",
    "referral_source" character varying(255),
    "status" character varying(100) NOT NULL,
    "result" character varying(30) NOT NULL,
    "created_by" character varying(255) NOT NULL,
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: pji_episodes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."pji_episodes_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pji_episodes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."pji_episodes_id_seq" OWNED BY "public"."pji_episodes"."id";


--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."role_permissions" (
    "role_id" bigint NOT NULL,
    "permission_id" bigint NOT NULL
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."roles" (
    "id" bigint NOT NULL,
    "name" character varying(255) NOT NULL,
    "description" character varying(255),
    "active" boolean DEFAULT true NOT NULL,
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "created_by" character varying(255),
    "updated_by" character varying(255),
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."roles_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."roles_id_seq" OWNED BY "public"."roles"."id";


--
-- Name: sensitivity_results; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."sensitivity_results" (
    "id" bigint NOT NULL,
    "culture_id" bigint NOT NULL,
    "antibiotic_name" character varying(100) NOT NULL,
    "mic_value" character varying(20),
    "sensitivity_code" character varying(10),
    "created_by" character varying(255),
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: sensitivity_results_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."sensitivity_results_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sensitivity_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."sensitivity_results_id_seq" OWNED BY "public"."sensitivity_results"."id";


--
-- Name: surgeries; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."surgeries" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "surgery_date" "date" NOT NULL,
    "surgery_type" character varying(255) NOT NULL,
    "findings" "text",
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_by" character varying(255),
    "created_by" character varying(255)
);


--
-- Name: surgeries_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."surgeries_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: surgeries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."surgeries_id_seq" OWNED BY "public"."surgeries"."id";


--
-- Name: treatment_plan_versions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."treatment_plan_versions" (
    "id" bigint NOT NULL,
    "episode_id" bigint NOT NULL,
    "source_run_id" bigint,
    "source_review_id" bigint,
    "version_no" integer NOT NULL,
    "is_current" boolean DEFAULT true NOT NULL,
    "status" "public"."treatment_plan_status" DEFAULT 'CONFIRMED'::"public"."treatment_plan_status" NOT NULL,
    "regimen_json" "jsonb" NOT NULL,
    "clinical_rationale" "text",
    "confirmed_by" bigint,
    "confirmed_at" timestamp with time zone,
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "public"."users" (
    "id" bigint NOT NULL,
    "role_id" bigint,
    "email" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "fullname" character varying(255) NOT NULL,
    "phone" character varying(255),
    "department" character varying(255),
    "is_active" boolean DEFAULT true NOT NULL,
    "last_login_at" timestamp with time zone,
    "created_by" character varying(255),
    "updated_by" character varying(255),
    "created_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "avatar" character varying(255),
    "refresh_token" "text",
    "status" character varying(50),
    "last_login" timestamp(6) with time zone
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "public"."users_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "public"."users_id_seq" OWNED BY "public"."users"."id";


--
-- Name: ai_chat_messages id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_messages" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."ai_chat_messages_id_seq"'::"regclass");


--
-- Name: ai_chat_sessions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_sessions" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."ai_chat_sessions_id_seq"'::"regclass");


--
-- Name: clinical_records id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."clinical_records" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."clinical_records_id_seq"'::"regclass");


--
-- Name: culture_results id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."culture_results" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."culture_results_id_seq"'::"regclass");


--
-- Name: image_results id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."image_results" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."image_results_id_seq"'::"regclass");


--
-- Name: lab_results id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."lab_results" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."lab_results_id_seq"'::"regclass");


--
-- Name: patients id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."patients" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."patients_id_seq"'::"regclass");


--
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."permissions" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."permissions_id_seq"'::"regclass");


--
-- Name: pji_episodes id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pji_episodes" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."pji_episodes_id_seq"'::"regclass");


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."roles" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."roles_id_seq"'::"regclass");


--
-- Name: sensitivity_results id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."sensitivity_results" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."sensitivity_results_id_seq"'::"regclass");


--
-- Name: surgeries id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."surgeries" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."surgeries_id_seq"'::"regclass");


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."users" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."users_id_seq"'::"regclass");


--
-- Name: ai_chat_messages ai_chat_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_messages"
    ADD CONSTRAINT "ai_chat_messages_pkey" PRIMARY KEY ("id");


--
-- Name: ai_chat_sessions ai_chat_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_sessions"
    ADD CONSTRAINT "ai_chat_sessions_pkey" PRIMARY KEY ("id");


--
-- Name: ai_rag_citations ai_rag_citations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_rag_citations"
    ADD CONSTRAINT "ai_rag_citations_pkey" PRIMARY KEY ("id");


--
-- Name: ai_recommendation_items ai_recommendation_items_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_items"
    ADD CONSTRAINT "ai_recommendation_items_pkey" PRIMARY KEY ("id");


--
-- Name: ai_recommendation_runs ai_recommendation_runs_episode_id_run_no_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_runs"
    ADD CONSTRAINT "ai_recommendation_runs_episode_id_run_no_key" UNIQUE ("episode_id", "run_no");


--
-- Name: ai_recommendation_runs ai_recommendation_runs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_runs"
    ADD CONSTRAINT "ai_recommendation_runs_pkey" PRIMARY KEY ("id");


--
-- Name: case_clinical_snapshots case_clinical_snapshots_episode_id_snapshot_no_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."case_clinical_snapshots"
    ADD CONSTRAINT "case_clinical_snapshots_episode_id_snapshot_no_key" UNIQUE ("episode_id", "snapshot_no");


--
-- Name: case_clinical_snapshots case_clinical_snapshots_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."case_clinical_snapshots"
    ADD CONSTRAINT "case_clinical_snapshots_pkey" PRIMARY KEY ("id");


--
-- Name: clinical_records clinical_records_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."clinical_records"
    ADD CONSTRAINT "clinical_records_pkey" PRIMARY KEY ("id");


--
-- Name: culture_results culture_results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."culture_results"
    ADD CONSTRAINT "culture_results_pkey" PRIMARY KEY ("id");


--
-- Name: doctor_recommendation_reviews doctor_recommendation_reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."doctor_recommendation_reviews"
    ADD CONSTRAINT "doctor_recommendation_reviews_pkey" PRIMARY KEY ("id");


--
-- Name: image_results image_results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."image_results"
    ADD CONSTRAINT "image_results_pkey" PRIMARY KEY ("id");


--
-- Name: lab_results lab_results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."lab_results"
    ADD CONSTRAINT "lab_results_pkey" PRIMARY KEY ("id");


--
-- Name: medical_histories medical_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."medical_histories"
    ADD CONSTRAINT "medical_histories_pkey" PRIMARY KEY ("episode_id");


--
-- Name: patients patients_patient_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."patients"
    ADD CONSTRAINT "patients_patient_code_key" UNIQUE ("patient_code");


--
-- Name: patients patients_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."patients"
    ADD CONSTRAINT "patients_pkey" PRIMARY KEY ("id");


--
-- Name: pending_lab_tasks pending_lab_tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pending_lab_tasks"
    ADD CONSTRAINT "pending_lab_tasks_pkey" PRIMARY KEY ("id");


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."permissions"
    ADD CONSTRAINT "permissions_pkey" PRIMARY KEY ("id");


--
-- Name: pji_episodes pji_episodes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pji_episodes"
    ADD CONSTRAINT "pji_episodes_pkey" PRIMARY KEY ("id");


--
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."role_permissions"
    ADD CONSTRAINT "role_permissions_pkey" PRIMARY KEY ("role_id", "permission_id");


--
-- Name: roles roles_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."roles"
    ADD CONSTRAINT "roles_name_key" UNIQUE ("name");


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."roles"
    ADD CONSTRAINT "roles_pkey" PRIMARY KEY ("id");


--
-- Name: sensitivity_results sensitivity_results_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."sensitivity_results"
    ADD CONSTRAINT "sensitivity_results_pkey" PRIMARY KEY ("id");


--
-- Name: surgeries surgeries_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."surgeries"
    ADD CONSTRAINT "surgeries_pkey" PRIMARY KEY ("id");


--
-- Name: treatment_plan_versions treatment_plan_versions_episode_id_version_no_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_episode_id_version_no_key" UNIQUE ("episode_id", "version_no");


--
-- Name: treatment_plan_versions treatment_plan_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_pkey" PRIMARY KEY ("id");


--
-- Name: pending_lab_tasks uq_pending_episode_field; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pending_lab_tasks"
    ADD CONSTRAINT "uq_pending_episode_field" UNIQUE ("episode_id", "field", "status");


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."users"
    ADD CONSTRAINT "users_email_key" UNIQUE ("email");


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."users"
    ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");


--
-- Name: idx_ai_chat_messages_session_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_chat_messages_session_created_at" ON "public"."ai_chat_messages" USING "btree" ("session_id", "created_at");


--
-- Name: idx_ai_chat_sessions_episode_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_chat_sessions_episode_id" ON "public"."ai_chat_sessions" USING "btree" ("episode_id");


--
-- Name: idx_ai_chat_sessions_run_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_chat_sessions_run_id" ON "public"."ai_chat_sessions" USING "btree" ("run_id");


--
-- Name: idx_ai_citations_run_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_citations_run_id" ON "public"."ai_rag_citations" USING "btree" ("run_id");


--
-- Name: idx_ai_items_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_items_category" ON "public"."ai_recommendation_items" USING "btree" ("category");


--
-- Name: idx_ai_items_json_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_items_json_gin" ON "public"."ai_recommendation_items" USING "gin" ("item_json");


--
-- Name: idx_ai_items_run_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_items_run_id" ON "public"."ai_recommendation_items" USING "btree" ("run_id");


--
-- Name: idx_ai_runs_assessment_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_runs_assessment_gin" ON "public"."ai_recommendation_runs" USING "gin" ("assessment_json");


--
-- Name: idx_ai_runs_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_runs_created_at" ON "public"."ai_recommendation_runs" USING "btree" ("created_at" DESC);


--
-- Name: idx_ai_runs_episode_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_runs_episode_id" ON "public"."ai_recommendation_runs" USING "btree" ("episode_id");


--
-- Name: idx_ai_runs_snapshot_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_ai_runs_snapshot_id" ON "public"."ai_recommendation_runs" USING "btree" ("snapshot_id");


--
-- Name: idx_clinical_records_episode_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_clinical_records_episode_time" ON "public"."clinical_records" USING "btree" ("episode_id", "recorded_at" DESC);


--
-- Name: idx_culture_results_episode_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_culture_results_episode_created_at" ON "public"."culture_results" USING "btree" ("episode_id", "created_at" DESC);


--
-- Name: idx_episodes_patient_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_episodes_patient_id" ON "public"."pji_episodes" USING "btree" ("patient_id");


--
-- Name: idx_episodes_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_episodes_status" ON "public"."pji_episodes" USING "btree" ("status");


--
-- Name: idx_image_results_episode_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_image_results_episode_created_at" ON "public"."image_results" USING "btree" ("episode_id", "created_at" DESC);


--
-- Name: idx_lab_results_episode_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_lab_results_episode_created_at" ON "public"."lab_results" USING "btree" ("episode_id", "created_at" DESC);


--
-- Name: idx_patients_identity_card; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_patients_identity_card" ON "public"."patients" USING "btree" ("identity_card");


--
-- Name: idx_plan_versions_current; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_plan_versions_current" ON "public"."treatment_plan_versions" USING "btree" ("episode_id", "is_current");


--
-- Name: idx_plan_versions_episode_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_plan_versions_episode_id" ON "public"."treatment_plan_versions" USING "btree" ("episode_id");


--
-- Name: idx_reviews_episode_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_reviews_episode_id" ON "public"."doctor_recommendation_reviews" USING "btree" ("episode_id");


--
-- Name: idx_reviews_run_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_reviews_run_id" ON "public"."doctor_recommendation_reviews" USING "btree" ("run_id");


--
-- Name: idx_reviews_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_reviews_status" ON "public"."doctor_recommendation_reviews" USING "btree" ("review_status");


--
-- Name: idx_sensitivity_results_culture_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_sensitivity_results_culture_id" ON "public"."sensitivity_results" USING "btree" ("culture_id");


--
-- Name: idx_snapshots_episode_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "idx_snapshots_episode_id" ON "public"."case_clinical_snapshots" USING "btree" ("episode_id");


--
-- Name: uq_treatment_plan_current_per_episode; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "uq_treatment_plan_current_per_episode" ON "public"."treatment_plan_versions" USING "btree" ("episode_id") WHERE ("is_current" = true);


--
-- Name: ai_chat_messages ai_chat_messages_session_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_messages"
    ADD CONSTRAINT "ai_chat_messages_session_id_fkey" FOREIGN KEY ("session_id") REFERENCES "public"."ai_chat_sessions"("id") ON DELETE CASCADE;


--
-- Name: ai_chat_sessions ai_chat_sessions_current_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_sessions"
    ADD CONSTRAINT "ai_chat_sessions_current_item_id_fkey" FOREIGN KEY ("current_item_id") REFERENCES "public"."ai_recommendation_items"("id");


--
-- Name: ai_chat_sessions ai_chat_sessions_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_sessions"
    ADD CONSTRAINT "ai_chat_sessions_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id");


--
-- Name: ai_chat_sessions ai_chat_sessions_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_chat_sessions"
    ADD CONSTRAINT "ai_chat_sessions_run_id_fkey" FOREIGN KEY ("run_id") REFERENCES "public"."ai_recommendation_runs"("id");


--
-- Name: ai_rag_citations ai_rag_citations_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_rag_citations"
    ADD CONSTRAINT "ai_rag_citations_item_id_fkey" FOREIGN KEY ("item_id") REFERENCES "public"."ai_recommendation_items"("id") ON DELETE CASCADE;


--
-- Name: ai_rag_citations ai_rag_citations_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_rag_citations"
    ADD CONSTRAINT "ai_rag_citations_run_id_fkey" FOREIGN KEY ("run_id") REFERENCES "public"."ai_recommendation_runs"("id") ON DELETE CASCADE;


--
-- Name: ai_recommendation_items ai_recommendation_items_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_items"
    ADD CONSTRAINT "ai_recommendation_items_run_id_fkey" FOREIGN KEY ("run_id") REFERENCES "public"."ai_recommendation_runs"("id") ON DELETE CASCADE;


--
-- Name: ai_recommendation_runs ai_recommendation_runs_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_runs"
    ADD CONSTRAINT "ai_recommendation_runs_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: ai_recommendation_runs ai_recommendation_runs_snapshot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."ai_recommendation_runs"
    ADD CONSTRAINT "ai_recommendation_runs_snapshot_id_fkey" FOREIGN KEY ("snapshot_id") REFERENCES "public"."case_clinical_snapshots"("id") ON DELETE CASCADE;


--
-- Name: case_clinical_snapshots case_clinical_snapshots_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."case_clinical_snapshots"
    ADD CONSTRAINT "case_clinical_snapshots_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: clinical_records clinical_records_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."clinical_records"
    ADD CONSTRAINT "clinical_records_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: culture_results culture_results_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."culture_results"
    ADD CONSTRAINT "culture_results_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: doctor_recommendation_reviews doctor_recommendation_reviews_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."doctor_recommendation_reviews"
    ADD CONSTRAINT "doctor_recommendation_reviews_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: doctor_recommendation_reviews doctor_recommendation_reviews_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."doctor_recommendation_reviews"
    ADD CONSTRAINT "doctor_recommendation_reviews_run_id_fkey" FOREIGN KEY ("run_id") REFERENCES "public"."ai_recommendation_runs"("id") ON DELETE CASCADE;


--
-- Name: pending_lab_tasks fkjmr2xh4reh2wmnr7pcxh6c7qd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pending_lab_tasks"
    ADD CONSTRAINT "fkjmr2xh4reh2wmnr7pcxh6c7qd" FOREIGN KEY ("patient_id") REFERENCES "public"."patients"("id");


--
-- Name: pending_lab_tasks fkpg8v9t854cmenkii5pckcxq64; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pending_lab_tasks"
    ADD CONSTRAINT "fkpg8v9t854cmenkii5pckcxq64" FOREIGN KEY ("fulfilled_lab_result_id") REFERENCES "public"."lab_results"("id");


--
-- Name: pending_lab_tasks fkspvdm09rf3k4q78t105b4tc7s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pending_lab_tasks"
    ADD CONSTRAINT "fkspvdm09rf3k4q78t105b4tc7s" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id");


--
-- Name: image_results image_results_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."image_results"
    ADD CONSTRAINT "image_results_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES "public"."users"("id");


--
-- Name: image_results image_results_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."image_results"
    ADD CONSTRAINT "image_results_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: image_results image_results_updated_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."image_results"
    ADD CONSTRAINT "image_results_updated_by_fkey" FOREIGN KEY ("updated_by") REFERENCES "public"."users"("id");


--
-- Name: lab_results lab_results_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."lab_results"
    ADD CONSTRAINT "lab_results_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: medical_histories medical_histories_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."medical_histories"
    ADD CONSTRAINT "medical_histories_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: pji_episodes pji_episodes_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."pji_episodes"
    ADD CONSTRAINT "pji_episodes_patient_id_fkey" FOREIGN KEY ("patient_id") REFERENCES "public"."patients"("id") ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_permission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."role_permissions"
    ADD CONSTRAINT "role_permissions_permission_id_fkey" FOREIGN KEY ("permission_id") REFERENCES "public"."permissions"("id") ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."role_permissions"
    ADD CONSTRAINT "role_permissions_role_id_fkey" FOREIGN KEY ("role_id") REFERENCES "public"."roles"("id") ON DELETE CASCADE;


--
-- Name: sensitivity_results sensitivity_results_culture_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."sensitivity_results"
    ADD CONSTRAINT "sensitivity_results_culture_id_fkey" FOREIGN KEY ("culture_id") REFERENCES "public"."culture_results"("id") ON DELETE CASCADE;


--
-- Name: surgeries surgeries_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."surgeries"
    ADD CONSTRAINT "surgeries_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: treatment_plan_versions treatment_plan_versions_confirmed_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_confirmed_by_fkey" FOREIGN KEY ("confirmed_by") REFERENCES "public"."users"("id");


--
-- Name: treatment_plan_versions treatment_plan_versions_episode_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_episode_id_fkey" FOREIGN KEY ("episode_id") REFERENCES "public"."pji_episodes"("id") ON DELETE CASCADE;


--
-- Name: treatment_plan_versions treatment_plan_versions_source_review_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_source_review_id_fkey" FOREIGN KEY ("source_review_id") REFERENCES "public"."doctor_recommendation_reviews"("id") ON DELETE SET NULL;


--
-- Name: treatment_plan_versions treatment_plan_versions_source_run_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."treatment_plan_versions"
    ADD CONSTRAINT "treatment_plan_versions_source_run_id_fkey" FOREIGN KEY ("source_run_id") REFERENCES "public"."ai_recommendation_runs"("id") ON DELETE SET NULL;


--
-- Name: users users_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "public"."users"
    ADD CONSTRAINT "users_role_id_fkey" FOREIGN KEY ("role_id") REFERENCES "public"."roles"("id");


--
-- PostgreSQL database dump complete
--

