# First stage
FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD

# Copy all staff
COPY ./ ./

# Package run
RUN mvn clean package

# Second stage
FROM adoptopenjdk/openjdk14

# Copy artifacts
COPY --from=MAVEN_BUILD /target/parsing-service-0.0.1-SNAPSHOT.jar /parsing-service.jar

# Startup
CMD ["java", "-jar", "/parsing-registry.jar"]

EXPOSE 8081