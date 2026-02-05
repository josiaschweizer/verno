#!/usr/bin/env bash
set -euo pipefail

REGION="europe-west6"
SERVICE="verno-app"
INSTANCE="verno-sql"

DEFAULT_MANDANT_ID="7777"
DEFAULT_MANDANT_NAME="Default Mandant"

# Default Settings
COURSE_WEEKS_PER_SCHEDULE="8"
MAX_PARTICIPANTS_PER_COURSE="12"
ENFORCE_QUANTITY_SETTINGS="true"
ENFORCE_COURSE_LEVEL_SETTINGS="false"
IS_PARENT_ONE_MAIN_PARENT="true"
COURSE_REPORT_NAME="course-report-"
LIMIT_COURSE_ASSIGNMENTS_TO_ACTIVE="false"

# Default Admin User
DEFAULT_ADMIN_USERNAME="admin"
DEFAULT_ADMIN_ROLE="ADMIN"
# admin1234 (bcrypt)
DEFAULT_ADMIN_PASSWORD_HASH='$2a$10$rA3knGig/VvsNSjPs2f2reSq41SqeS3QzyQGEK/mpRdD86lvYufQG'

PROJECT_ID="$(gcloud config get-value project)"
CONN_NAME="$(gcloud sql instances describe "${INSTANCE}" --format='value(connectionName)')"

# Cloud Run URL -> Host extrahieren
SERVICE_URL="$(gcloud run services describe "${SERVICE}" --region "${REGION}" --format='value(status.url)')"
HOST="$(echo "${SERVICE_URL}" | sed -E 's#^https?://##' | sed -E 's#/.*$##')"

# Wichtig: MandantResolver nimmt nur das erste Label (bis zum ersten Punkt)
SLUG="${HOST%%.*}"

echo "Project: ${PROJECT_ID}"
echo "Service: ${SERVICE}"
echo "Service URL: ${SERVICE_URL}"
echo "Host: ${HOST}"
echo "Default Mandant ID: ${DEFAULT_MANDANT_ID}"
echo "Slug: ${SLUG}"

DB_USER="$(gcloud secrets versions access latest --secret=verno_db_user)"
DB_PASS="$(gcloud secrets versions access latest --secret=verno_db_pass)"
DB_NAME="$(gcloud secrets versions access latest --secret=verno_db_name)"

# Cloud SQL Proxy starten
PROXY_PORT="5433"
cloud-sql-proxy --address 127.0.0.1 --port "${PROXY_PORT}" "${CONN_NAME}" >/tmp/cloud-sql-proxy.log 2>&1 &
PROXY_PID=$!

cleanup() {
  kill "${PROXY_PID}" >/dev/null 2>&1 || true
}
trap cleanup EXIT

# kurz warten bis Proxy ready ist
sleep 1

export PGPASSWORD="${DB_PASS}"

psql "host=127.0.0.1 port=${PROXY_PORT} dbname=${DB_NAME} user=${DB_USER} sslmode=disable" \
  -v ON_ERROR_STOP=1 \
  -v mandant_id="${DEFAULT_MANDANT_ID}" \
  -v mandant_name="${DEFAULT_MANDANT_NAME}" \
  -v slug="${SLUG}" \
  -v course_weeks="${COURSE_WEEKS_PER_SCHEDULE}" \
  -v max_participants="${MAX_PARTICIPANTS_PER_COURSE}" \
  -v enforce_quantity="${ENFORCE_QUANTITY_SETTINGS}" \
  -v enforce_levels="${ENFORCE_COURSE_LEVEL_SETTINGS}" \
  -v parent_one_main="${IS_PARENT_ONE_MAIN_PARENT}" \
  -v report_name="${COURSE_REPORT_NAME}" \
  -v limit_active="${LIMIT_COURSE_ASSIGNMENTS_TO_ACTIVE}" \
  -v admin_user="${DEFAULT_ADMIN_USERNAME}" \
  -v admin_role="${DEFAULT_ADMIN_ROLE}" \
  -v admin_hash="${DEFAULT_ADMIN_PASSWORD_HASH}" \
  <<'SQL'
BEGIN;

-- Check: slug bereits vergeben (DB kann leer sein, dann false)
SELECT EXISTS (
  SELECT 1
  FROM public.mandants
  WHERE slug = :'slug'
) AS slug_taken
\gset

\if :slug_taken
  \echo 'ERROR: slug already exists: ' :slug
  ROLLBACK;
  \quit 1
\endif

-- 1) Mandant 7777 anlegen
INSERT INTO public.mandants (id, slug, name)
VALUES (:'mandant_id'::bigint, :'slug', :'mandant_name');

-- 2) Default Settings für Mandant 7777
INSERT INTO public.mandant_settings (
  id,
  course_weeks_per_schedule,
  max_participants_per_course,
  enforce_quantity_settings,
  enforce_course_level_settings,
  is_parent_one_main_parent,
  course_report_name,
  limit_course_assignments_to_active
)
VALUES (
  :'mandant_id'::bigint,
  :'course_weeks'::int,
  :'max_participants'::int,
  :'enforce_quantity'::boolean,
  :'enforce_levels'::boolean,
  :'parent_one_main'::boolean,
  :'report_name',
  :'limit_active'::boolean
);

-- 3) Default Admin User (admin / admin1234)
INSERT INTO public.app_user (mandant_id, username, password_hash, role)
VALUES (
  :'mandant_id'::bigint,
  :'admin_user',
  :'admin_hash',
  :'admin_role'
);

-- 4) Default Admin User Settings
INSERT INTO public.app_user_settings (mandant_id, user_id, theme, language_tag)
SELECT
  :'mandant_id'::bigint,
  u.id,
  'default',
  'de'
FROM public.app_user u
WHERE u.mandant_id = :'mandant_id'::bigint
  AND u.username = :'admin_user';

COMMIT;
SQL

echo "Done."
echo "- mandant_id: ${DEFAULT_MANDANT_ID}"
echo "- slug: ${SLUG}"
echo "- admin user: ${DEFAULT_ADMIN_USERNAME} (pw: admin1234)"