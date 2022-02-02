# First stage
FROM maven:3.8.4-openjdk-11 AS MAVEN_BUILD

# Copy all staff
COPY ./ ./

# Package run
RUN mvn clean package

# Second stage
FROM adoptopenjdk/openjdk11

# Copy artifacts
COPY --from=MAVEN_BUILD /target/timetable-service-registry.jar /service-registry.jar

# Startup
CMD ["java", "-jar", "/service-registry.jar"]