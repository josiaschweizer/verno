#!/usr/bin/env bash
set -euo pipefail

# -----------------------
# Lokal: DB Connection
# -----------------------
DB_HOST="${DB_HOST:-127.0.0.1}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-verno}"
DB_USER="${DB_USER:-verno}"
DB_PASS="${DB_PASS:-verno}"

if [[ -z "${DB_PASS}" ]]; then
  echo "ERROR: DB_PASS is not set."
  echo "Example: DB_PASS=secret ./scripts/local/default-1111.sh"
  exit 1
fi

# -----------------------
# Mandant 1111 (lokal)
# -----------------------
MANDANT_ID="1111"
MANDANT_SLUG="${MANDANT_SLUG:-default-1111}"
MANDANT_NAME="${MANDANT_NAME:-Default Mandant 1111}"

# Default Settings
COURSE_WEEKS_PER_SCHEDULE="8"
MAX_PARTICIPANTS_PER_COURSE="12"
ENFORCE_QUANTITY_SETTINGS="true"
ENFORCE_COURSE_LEVEL_SETTINGS="false"
IS_PARENT_ONE_MAIN_PARENT="true"
COURSE_REPORT_NAME="course-report-"
LIMIT_COURSE_ASSIGNMENTS_TO_ACTIVE="false"

# Admin User
ADMIN_USERNAME="${ADMIN_USERNAME:-admin}"
ADMIN_ROLE="ADMIN"
# admin1234 (bcrypt)
ADMIN_PASSWORD_HASH='${ADMIN_PASSWORD_HASH:-$2a$10$rA3knGig/VvsNSjPs2f2reSq41SqeS3QzyQGEK/mpRdD86lvYufQG}'

export PGPASSWORD="${DB_PASS}"

echo "Seeding local DB..."
echo "- host: ${DB_HOST}:${DB_PORT}"
echo "- db:   ${DB_NAME}"
echo "- user: ${DB_USER}"
echo "- mandant_id: ${MANDANT_ID}"
echo "- slug: ${MANDANT_SLUG}"
echo "- name: ${MANDANT_NAME}"

psql "host=${DB_HOST} port=${DB_PORT} dbname=${DB_NAME} user=${DB_USER} sslmode=disable" \
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

-- 0) Check: slug bereits vergeben
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

-- 1) Mandant anlegen (idempotent via id)
INSERT INTO public.mandants (id, slug, name)
VALUES (:'mandant_id'::bigint, :'slug', :'mandant_name')
ON CONFLICT (id) DO UPDATE
SET slug = EXCLUDED.slug,
    name = EXCLUDED.name;

-- 2) Default Settings für Mandant
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
)
ON CONFLICT (id) DO UPDATE SET
  course_weeks_per_schedule = EXCLUDED.course_weeks_per_schedule,
  max_participants_per_course = EXCLUDED.max_participants_per_course,
  enforce_quantity_settings = EXCLUDED.enforce_quantity_settings,
  enforce_course_level_settings = EXCLUDED.enforce_course_level_settings,
  is_parent_one_main_parent = EXCLUDED.is_parent_one_main_parent,
  course_report_name = EXCLUDED.course_report_name,
  limit_course_assignments_to_active = EXCLUDED.limit_course_assignments_to_active;

-- 3) Admin User upsert (requires uk_app_user_mandant_username UNIQUE (mandant_id, username))
INSERT INTO public.app_user (mandant_id, username, password_hash, role)
VALUES (
  :'mandant_id'::bigint,
  :'admin_user',
  :'admin_hash',
  :'admin_role'
)
ON CONFLICT (mandant_id, username) DO UPDATE
SET password_hash = EXCLUDED.password_hash,
    role = EXCLUDED.role;

-- 4) Admin User Settings: delete + insert (no unique constraint required)
DELETE FROM public.app_user_settings s
USING public.app_user u
WHERE s.user_id = u.id
  AND s.mandant_id = :'mandant_id'::bigint
  AND u.mandant_id = :'mandant_id'::bigint
  AND u.username = :'admin_user';

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
echo "- admin user: ${ADMIN_USERNAME} (pw: admin1234)"
echo "- local db: ${DB_HOST}:${DB_PORT}/${DB_NAME}"