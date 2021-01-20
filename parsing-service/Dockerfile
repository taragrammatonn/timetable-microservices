FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} parsing-service.jar
ENTRYPOINT ["java","-jar","/parsing-service.jar"]
EXPOSE 8081