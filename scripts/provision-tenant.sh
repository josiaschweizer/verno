#!/usr/bin/env bash
set -euo pipefail

- usage() {
  cat <<'EOF'
Usage:
  ./provision-tenant.sh --slug flipper --image-name verno-app --tag beta-1

Optional:
  --project <gcp-project-id>         (default: current gcloud project)
  --region <region>                  (default: europe-west6)
  --service-prefix <prefix>          (default: verno)
  --db-version <POSTGRES_16>         (default: POSTGRES_16)
  --db-tier <tier>                   (default: db-perf-optimized-N-2)
  --db-storage <gb>                  (default: 20)
  --runtime-sa <service-account>     (default: verno-runtime@<project>.iam.gserviceaccount.com)
  --allow-unauthenticated <true|false> (default: true)
  --skip-build <true|false>          (default: false)

Beispiel:
  ./provision-tenant.sh --slug flipper --image-name verno-app --tag beta-1 --db-tier db-perf-optimized-N-8
EOF
}

- require_cmd() {
  command -v "$1" >/dev/null 2>&1 || { echo "Missing command: $1"; exit 1; }
}

- bool_is_true() {
  [[ "${1:-}" == "true" || "${1:-}" == "1" || "${1:-}" == "yes" ]]
}

- wait_sql_runnable() {
  local instance="$1"
  echo "Waiting for Cloud SQL instance to become RUNNABLE: ${instance}"
  while true; do
    local state
    state="$(gcloud sql instances describe "${instance}" --format="value(state)" 2>/dev/null || true)"
    if [[ "${state}" == "RUNNABLE" ]]; then
      echo "Cloud SQL instance is RUNNABLE."
      break
    fi
    if [[ -n "${state}" ]]; then
      echo "  state=${state} (sleep 10s)"
    else
      echo "  state=<unknown> (sleep 10s)"
    fi
    sleep 10
  done
}

- parse args
SLUG=""
IMAGE_NAME=""
TAG=""

PROJECT_ID=""
REGION="europe-west6"
SERVICE_PREFIX="verno"

DB_VERSION="POSTGRES_16"
DB_TIER="db-perf-optimized-N-2"
DB_STORAGE="20"

RUNTIME_SA=""
ALLOW_UNAUTH="true"
SKIP_BUILD="false"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --slug) SLUG="${2:-}"; shift 2 ;;
    --image-name) IMAGE_NAME="${2:-}"; shift 2 ;;
    --tag) TAG="${2:-}"; shift 2 ;;

    --project) PROJECT_ID="${2:-}"; shift 2 ;;
    --region) REGION="${2:-}"; shift 2 ;;
    --service-prefix) SERVICE_PREFIX="${2:-}"; shift 2 ;;

    --db-version) DB_VERSION="${2:-}"; shift 2 ;;
    --db-tier) DB_TIER="${2:-}"; shift 2 ;;
    --db-storage) DB_STORAGE="${2:-}"; shift 2 ;;

    --runtime-sa) RUNTIME_SA="${2:-}"; shift 2 ;;
    --allow-unauthenticated) ALLOW_UNAUTH="${2:-}"; shift 2 ;;
    --skip-build) SKIP_BUILD="${2:-}"; shift 2 ;;

    -h|--help) usage; exit 0 ;;
    *) echo "Unknown arg: $1"; usage; exit 1 ;;
  esac
done

if [[ -z "${SLUG}" || -z "${IMAGE_NAME}" || -z "${TAG}" ]]; then
  echo "Missing required args."
  usage
  exit 1
fi

require_cmd gcloud
require_cmd openssl

if [[ -z "${PROJECT_ID}" ]]; then
  PROJECT_ID="$(gcloud config get-value project 2>/dev/null || true)"
fi
if [[ -z "${PROJECT_ID}" ]]; then
  echo "No project set. Run: gcloud config set project <id>"
  exit 1
fi

if [[ -z "${RUNTIME_SA}" ]]; then
  RUNTIME_SA="verno-runtime@${PROJECT_ID}.iam.gserviceaccount.com"
fi

- derived names
SERVICE="${SERVICE_PREFIX}-${SLUG}"
AR_REPO="${SERVICE_PREFIX}-${SLUG}"

SQL_INSTANCE="${SERVICE_PREFIX}-${SLUG}-db"
DB_NAME="${SERVICE_PREFIX}_${SLUG}"
DB_USER="${SERVICE_PREFIX}_${SLUG}_app"

SECRET_DB_PASS="${SERVICE}-db-pass"

IMAGE="${REGION}-docker.pkg.dev/${PROJECT_ID}/${AR_REPO}/${IMAGE_NAME}:${TAG}"

echo "Project: ${PROJECT_ID}"
echo "Region: ${REGION}"
echo "Tenant slug: ${SLUG}"
echo "Cloud Run service: ${SERVICE}"
echo "Artifact Repo: ${AR_REPO}"
echo "Image: ${IMAGE}"
echo "Cloud SQL instance: ${SQL_INSTANCE}"
echo "DB: ${DB_NAME}"
echo "DB user: ${DB_USER}"
echo "Runtime SA: ${RUNTIME_SA}"

- enable required APIs (idempotent)
gcloud services enable \
  run.googleapis.com \
  artifactregistry.googleapis.com \
  cloudbuild.googleapis.com \
  sqladmin.googleapis.com \
  secretmanager.googleapis.com

- Artifact Registry repo (create if missing)
if gcloud artifacts repositories describe "${AR_REPO}" --location="${REGION}" >/dev/null 2>&1; then
  echo "Artifact Registry repo exists: ${AR_REPO}"
else
  echo "Creating Artifact Registry repo: ${AR_REPO}"
  gcloud artifacts repositories create "${AR_REPO}" \
    --repository-format=docker \
    --location="${REGION}" \
    --description="Images for ${SERVICE} (CH)"
fi

- docker auth for artifact registry
gcloud auth configure-docker "${REGION}-docker.pkg.dev" -q

- build & push
if bool_is_true "${SKIP_BUILD}"; then
  echo "Skipping build as requested: --skip-build=true"
else
  echo "Building + pushing image via Cloud Build: ${IMAGE}"
  gcloud builds submit --tag "${IMAGE}"
fi

- Cloud SQL instance (create if missing)
if gcloud sql instances describe "${SQL_INSTANCE}" >/dev/null 2>&1; then
  echo "Cloud SQL instance exists: ${SQL_INSTANCE}"
else
  echo "Creating Cloud SQL instance: ${SQL_INSTANCE}"
  echo "  db-tier=${DB_TIER}, db-storage=${DB_STORAGE}GB, db-version=${DB_VERSION}"
  gcloud sql instances create "${SQL_INSTANCE}" \
    --database-version="${DB_VERSION}" \
    --region="${REGION}" \
    --tier="${DB_TIER}" \
    --storage-size="${DB_STORAGE}" \
    --storage-type=SSD \
    --backup-start-time=03:00 \
    --availability-type=zonal
fi

wait_sql_runnable "${SQL_INSTANCE}"

INSTANCE_CONNECTION_NAME="$(gcloud sql instances describe "${SQL_INSTANCE}" --format="value(connectionName)")"
echo "Instance connection name: ${INSTANCE_CONNECTION_NAME}"

- Secret for DB password (create or add version)
DB_PASS="$(openssl rand -base64 36 | tr -d '\n' | tr -d '/' | tr -d '+')"

if gcloud secrets describe "${SECRET_DB_PASS}" >/dev/null 2>&1; then
  echo "Secret exists, adding new version: ${SECRET_DB_PASS}"
  printf "%s" "${DB_PASS}" | gcloud secrets versions add "${SECRET_DB_PASS}" --data-file=-
else
  echo "Creating secret (user-managed, CH-only): ${SECRET_DB_PASS}"
  printf "%s" "${DB_PASS}" | gcloud secrets create "${SECRET_DB_PASS}" \
    --replication-policy="user-managed" \
    --locations="${REGION}" \
    --data-file=-
fi

- database create if missing
if gcloud sql databases describe "${DB_NAME}" --instance="${SQL_INSTANCE}" >/dev/null 2>&1; then
  echo "Database exists: ${DB_NAME}"
else
  echo "Creating database: ${DB_NAME}"
  gcloud sql databases create "${DB_NAME}" --instance="${SQL_INSTANCE}"
fi

- user create (idempotent-ish: if exists, set password)
if gcloud sql users list --instance="${SQL_INSTANCE}" --format="value(name)" | grep -qx "${DB_USER}"; then
  echo "DB user exists, setting new password: ${DB_USER}"
  gcloud sql users set-password "${DB_USER}" \
    --instance="${SQL_INSTANCE}" \
    --password="${DB_PASS}"
else
  echo "Creating DB user: ${DB_USER}"
  gcloud sql users create "${DB_USER}" \
    --instance="${SQL_INSTANCE}" \
    --password="${DB_PASS}"
fi

- Cloud Run deploy
ALLOW_FLAG="--no-allow-unauthenticated"
if bool_is_true "${ALLOW_UNAUTH}"; then
  ALLOW_FLAG="--allow-unauthenticated"
fi

echo "Deploying Cloud Run service: ${SERVICE}"

gcloud run deploy "${SERVICE}" \
  --image="${IMAGE}" \
  --region="${REGION}" \
  --platform=managed \
  --service-account="${RUNTIME_SA}" \
  --add-cloudsql-instances="${INSTANCE_CONNECTION_NAME}" \
  --set-env-vars="DB_NAME=${DB_NAME},DB_USER=${DB_USER},DB_HOST=/cloudsql/${INSTANCE_CONNECTION_NAME},DB_PORT=5432" \
  --set-secrets="DB_PASS=${SECRET_DB_PASS}:latest" \
  ${ALLOW_FLAG}

SERVICE_URL="$(gcloud run services describe "${SERVICE}" --region="${REGION}" --format="value(status.url)")"
echo "Deployed: ${SERVICE_URL}"
echo "Done."