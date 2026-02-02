#!/usr/bin/env bash
set -e

# -----------------------------
# Projekt setzen
# -----------------------------
gcloud config set project keen-jigsaw-482516-e6

# -----------------------------
# Benötigte APIs aktivieren
# -----------------------------
gcloud services enable \
  run.googleapis.com \
  sqladmin.googleapis.com \
  secretmanager.googleapis.com \
  artifactregistry.googleapis.com \
  iam.googleapis.com

# -----------------------------
# Cloud SQL Postgres Instance (CH, Zurich)
# -----------------------------
gcloud sql instances create verno-sql-ch \
  --database-version=POSTGRES_16 \
  --edition=ENTERPRISE_PLUS \
  --region=europe-west6 \
  --availability-type=ZONAL \
  --tier=db-perf-optimized-N-2 \
  --storage-type=SSD \
  --storage-size=20 \
  --storage-auto-increase \
  --backup-start-time=03:00 \
  --maintenance-window-day=SUN \
  --maintenance-window-hour=04 \
  --maintenance-release-channel=production

# -----------------------------
# Datenbanken
# -----------------------------
gcloud sql databases create verno_control \
  --instance=verno-sql-ch

gcloud sql databases create verno_app \
  --instance=verno-sql-ch

# -----------------------------
# DB Passwort generieren
# (wichtig: nur lokal einmal!)
# -----------------------------
DB_PASS="$(openssl rand -base64 32 | tr -d '\n')"

# -----------------------------
# DB User
# -----------------------------
gcloud sql users create verno_app \
  --instance=verno-sql-ch \
  --password="$DB_PASS"

# -----------------------------
# Secrets (CH-only, user-managed)
# -----------------------------
printf "%s" "$DB_PASS" | gcloud secrets create verno-db-password \
  --replication-policy="user-managed" \
  --locations="europe-west6" \
  --data-file=-

gcloud secrets create verno-db-user \
  --replication-policy="user-managed" \
  --locations="europe-west6" \
  --data-file=- <<< "verno_app"

gcloud secrets create verno-db-name-control \
  --replication-policy="user-managed" \
  --locations="europe-west6" \
  --data-file=- <<< "verno_control"

gcloud secrets create verno-db-name-app \
  --replication-policy="user-managed" \
  --locations="europe-west6" \
  --data-file=- <<< "verno_app"

gcloud secrets create verno-sql-connection-name \
  --replication-policy="user-managed" \
  --locations="europe-west6" \
  --data-file=- <<< "keen-jigsaw-482516-e6:europe-west6:verno-sql-ch"

echo "Setup completed successfully."