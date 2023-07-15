# Base image with Java and Maven
FROM maven:3.8.4-openjdk-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Production-ready image with JRE
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/roleService-0.0.1-SNAPSHOT.jar .

# Expose the port used by the application
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "roleService-0.0.1-SNAPSHOT.jar"]
