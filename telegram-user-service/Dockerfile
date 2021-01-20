FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} telegram-user-service.jar
ENTRYPOINT ["java","-jar","/telegram-user-service.jar"]
EXPOSE 8082