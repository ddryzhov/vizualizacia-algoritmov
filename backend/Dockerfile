# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
# Set working directory inside the build container
WORKDIR /app
# Copy the entire project into the build container
COPY . .
# Package the application, skipping tests for faster builds
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM openjdk:17-jdk-slim
# Set working directory in the runtime container
WORKDIR /app
# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose application port (default Spring Boot port)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
