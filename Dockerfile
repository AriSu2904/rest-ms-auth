# Base image
FROM openjdk:17-jdk-alpine

# Maintainer info
LABEL maintainer="ari.susanto"

# Copy JAR file into the container
COPY target/authorization-0.0.1-SNAPSHOT.jar /auth-service.jar

# Expose the port that the application runs on (ubah sesuai port aplikasi Anda, misalnya 8080)
EXPOSE 3002

COPY src/main/resources/private_key.pem src/main/resources/private_key.pem
COPY src/main/resources/public_key.pem src/main/resources/public_key.pem

# Default command to run the jar file
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]
