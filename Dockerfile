FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ui/target/ui-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
