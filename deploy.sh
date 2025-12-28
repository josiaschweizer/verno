#!/usr/bin/env bash
set -euo pipefail

PROJECT_ID="keen-jigsaw-482516-e6"
REGION="europe-west6"
SERVICE_NAME="verno-ui"
REPO="verno"
IMAGE_NAME="verno-ui"

IMAGE="${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO}/${IMAGE_NAME}:latest"
APP_JAR="app.jar"

echo "==> 0/3 Preflight"
echo "PROJECT : ${PROJECT_ID}"
echo "REGION  : ${REGION}"
echo "SERVICE : ${SERVICE_NAME}"
echo "IMAGE   : ${IMAGE}"

if [[ ! -f "${APP_JAR}" ]]; then
  echo "ERROR: ${APP_JAR} nicht gefunden!"
  echo "→ Bitte vorher manuell bauen und ins Root legen."
  exit 1
fi

echo "==> 1/3 Build & Push Image"
gcloud builds submit --tag "${IMAGE}"

echo "==> 2/3 Deploy to Cloud Run"
gcloud run services update "${SERVICE_NAME}" \
  --project "${PROJECT_ID}" \
  --region "${REGION}" \
  --image "${IMAGE}"

echo "==> 3/3 Done"
echo "Service URL:"
gcloud run services describe "${SERVICE_NAME}" \
  --region "${REGION}" \
  --format="value(status.url)"