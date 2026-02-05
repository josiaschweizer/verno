PROJECT_ID="keen-jigsaw-482516-e6"
REGION="europe-west6"
REPO="verno"
IMAGE="verno-app"
SERVICE="verno-app"

TAG="$(date +%Y%m%d-%H%M%S)"

gcloud builds submit \
  --project "${PROJECT_ID}" \
  --tag "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO}/${IMAGE}:${TAG}" \
  .

gcloud run deploy "${SERVICE}" \
  --project "${PROJECT_ID}" \
  --region "${REGION}" \
  --image "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO}/${IMAGE}:${TAG}" \
  --platform managed \
  --allow-unauthenticated \
  --service-account "verno-run-sa@${PROJECT_ID}.iam.gserviceaccount.com" \
  --add-cloudsql-instances "${CLOUDSQL_INSTANCE}" \
  --set-env-vars "SPRING_PROFILES_ACTIVE=prod,CLOUDSQL_INSTANCE=${CLOUDSQL_INSTANCE}" \
  --set-secrets "DB_USER=verno_db_user:latest,DB_PASS=verno_db_pass:latest,DB_NAME=verno_db_name:latest"