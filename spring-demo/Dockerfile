# Use the official Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK to run the application
FROM openjdk:17-jdk-slim

# Copy the built JAR from the build stage
COPY --from=build /app/target/ds-2020-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]