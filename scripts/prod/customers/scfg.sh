#!/usr/bin/env bash
set -euo pipefail

REGION="europe-west6"
SERVICE="verno-app"
INSTANCE="verno-sql"

MANDANT_ID="1"
MANDANT_SLUG="scfg"
MANDANT_NAME="Schwimmclub Flipper Gossau"

# Default Settings
COURSE_WEEKS_PER_SCHEDULE="8"
MAX_PARTICIPANTS_PER_COURSE="12"
ENFORCE_QUANTITY_SETTINGS="true"
ENFORCE_COURSE_LEVEL_SETTINGS="false"
IS_PARENT_ONE_MAIN_PARENT="true"
COURSE_REPORT_NAME="course-report-"
LIMIT_COURSE_ASSIGNMENTS_TO_ACTIVE="false"

# Admin User
ADMIN_USERNAME="admin"
ADMIN_ROLE="ADMIN"
# admin1234! (bcrypt)
ADMIN_PASSWORD_HASH='$2a$10$D1UGRPxLlopdHvRT26k7JePfLfNRTBQZ7oHDwrvprqBFnvBBgnQ2m'

PROJECT_ID="$(gcloud config get-value project)"
CONN_NAME="$(gcloud sql instances describe "${INSTANCE}" --format='value(connectionName)')"

SERVICE_URL="$(gcloud run services describe "${SERVICE}" --region "${REGION}" --format='value(status.url)')"
HOST="scfg.verno-appc.ch"

echo "Project: ${PROJECT_ID}"
echo "Service: ${SERVICE}"
echo "Service URL: ${SERVICE_URL}"
echo "Host: ${HOST}"
echo "Mandant ID: ${MANDANT_ID}"
echo "Slug: ${MANDANT_SLUG}"
echo "Name: ${MANDANT_NAME}"

DB_USER="$(gcloud secrets versions access latest --secret=verno_db_user)"
DB_PASS="$(gcloud secrets versions access latest --secret=verno_db_pass)"
DB_NAME="$(gcloud secrets versions access latest --secret=verno_db_name)"

PROXY_PORT="5433"
cloud-sql-proxy --address 127.0.0.1 --port "${PROXY_PORT}" "${CONN_NAME}" >/tmp/cloud-sql-proxy.log 2>&1 &
PROXY_PID=$!

cleanup() {
  kill "${PROXY_PID}" >/dev/null 2>&1 || true
}
trap cleanup EXIT

sleep 1

export PGPASSWORD="${DB_PASS}"

psql "host=127.0.0.1 port=${PROXY_PORT} dbname=${DB_NAME} user=${DB_USER} sslmode=disable" \
  -v ON_ERROR_STOP=1 \
  -v mandant_id="${MANDANT_ID}" \
  -v mandant_name="${MANDANT_NAME}" \
  -v slug="${MANDANT_SLUG}" \
  -v course_weeks="${COURSE_WEEKS_PER_SCHEDULE}" \
  -v max_participants="${MAX_PARTICIPANTS_PER_COURSE}" \
  -v enforce_quantity="${ENFORCE_QUANTITY_SETTINGS}" \
  -v enforce_levels="${ENFORCE_COURSE_LEVEL_SETTINGS}" \
  -v parent_one_main="${IS_PARENT_ONE_MAIN_PARENT}" \
  -v report_name="${COURSE_REPORT_NAME}" \
  -v limit_active="${LIMIT_COURSE_ASSIGNMENTS_TO_ACTIVE}" \
  -v admin_user="${ADMIN_USERNAME}" \
  -v admin_role="${ADMIN_ROLE}" \
  -v admin_hash="${ADMIN_PASSWORD_HASH}" \
  <<'SQL'
BEGIN;

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

INSERT INTO public.mandants (id, slug, name)
VALUES (:'mandant_id'::bigint, :'slug', :'mandant_name');

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

INSERT INTO public.app_user (mandant_id, username, password_hash, role)
VALUES (
  :'mandant_id'::bigint,
  :'admin_user',
  :'admin_hash',
  :'admin_role'
);

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
echo "- mandant_id: ${MANDANT_ID}"
echo "- slug: ${MANDANT_SLUG}"
echo "- admin user: ${ADMIN_USERNAME} (pw: admin1234!)"