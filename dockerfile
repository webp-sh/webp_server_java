FROM openjdk:8-jre-alpine

ARG JAR_FILE=build/libs/*.jar
ARG CONFIG_FILE=config-example.json

COPY ${JAR_FILE} webp-server-java.jar
COPY ${CONFIG_FILE} config.json

EXPOSE 3333
EXPOSE 8080
ENTRYPOINT java -jar webp-server.jar config.json