flyway \
  -url="jdbc:postgresql://localhost:5432/verno" \
  -user="verno" \
  -password="verno" \
  -locations="filesystem:./ui/src/main/resources/db/migration/common,filesystem:./ui/src/main/resources/db/migration/dev" \
  repair