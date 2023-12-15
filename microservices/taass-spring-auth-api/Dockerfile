# Versione con compilazione fatta dentro il container docker
FROM maven:3.6-jdk-8-alpine as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package

FROM openjdk:8u212-jre-alpine
EXPOSE 80
WORKDIR /usr/local/service
COPY --from=maven target/taass-spring-auth-api.jar ./
ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=${SUSPEND},address=5005 -jar taass-spring-auth-api.jar


# Versione con compilazione fatta da netbeans
# FROM openjdk:8-alpine
# ADD target/taass-spring-auth-api.jar taass-spring-auth-api.jar
# EXPOSE 80
# ENTRYPOINT ["java", "-jar", "taass-spring-auth-api.jar"]