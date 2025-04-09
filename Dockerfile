FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/service-0.0.1-SNAPSHOT.jar song-service.jar
"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENTRYPOINT ["java", "-jar", "song-service.jar"]
