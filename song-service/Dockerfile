# ====== Stage 1: Build stage ======
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true
# ====== Stage 1: Build stage ======
FROM maven:3.9.4-eclipse-temurin-21 AS runtime

WORKDIR /app
# Copy the JAR file built by Maven
COPY --from=build /app/target/*.jar song-service.jar

# Run the application
ENTRYPOINT ["java", "-jar", "song-service.jar"]