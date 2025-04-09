FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/service-0.0.1-SNAPSHOT.jar song-service.jar

ENTRYPOINT ["java", "-jar", "song-service.jar"]
