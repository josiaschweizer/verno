flyway \
  -url="jdbc:postgresql://localhost:5432/verno" \
  -user="verno" \
  -password="verno" \
  -locations="filesystem:./src/main/resources/db/migration/common,filesystem:./src/main/resources/db/migration/dev" \
  repair