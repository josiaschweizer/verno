#!/usr/bin/env bash
set -euo pipefail

echo "==> 1/4 Build mit Maven"
mvn clean package -DskipTests

JAR_SOURCE="ui/target/ui-1.0-SNAPSHOT.jar"
JAR_TARGET="app.jar"

echo "==> 2/4 Prüfe ob JAR existiert"
if [ ! -f "$JAR_SOURCE" ]; then
  echo "❌ Fehler: $JAR_SOURCE wurde nicht gefunden"
  exit 1
fi

echo "==> 3/4 Lösche bestehendes app.jar (falls vorhanden)"
rm -f "$JAR_TARGET"

echo "==> 4/4 Kopiere und benenne JAR"
cp "$JAR_SOURCE" "$JAR_TARGET"

echo "✅  Fertig: app.jar wurde aktualisiert"