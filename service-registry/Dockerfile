# First stage
FROM maven:3.8.4-openjdk-11 AS MAVEN_BUILD

# Copy all staff
COPY ./ ./

# Package run
RUN mvn clean package

# Second stage
FROM openjdk:11.0.7-jdk-slim

# Copy artifacts
COPY --from=MAVEN_BUILD /target/service-registry-0.0.1-SNAPSHOT.jar /service-registry.jar

# Startup
CMD ["java", "-jar", "/service-registry.jar"]

EXPOSE 8761