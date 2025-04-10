# Use OpenJDK base image
FROM openjdk:23-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file built by Maven
COPY target/mp3resourceservice-0.0.1-SNAPSHOT.jar mp3processing.jar

# Run the application
ENTRYPOINT ["java", "-jar", "mp3processing.jar"]
