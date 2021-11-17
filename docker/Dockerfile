
FROM openjdk:8-jdk-alpine
MAINTAINER Russle Da Progger <iwontgiveyoumyemail@yeahright.ru>
ENV TZ=Europe/Moscow
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} docker-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/docker-0.0.1.jar"]
CMD ["--spring.profiles.active=prod"]