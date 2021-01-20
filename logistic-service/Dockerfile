FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} logistic-service.jar
ENTRYPOINT ["java","-jar","/logistic-service.jar"]
EXPOSE 8083