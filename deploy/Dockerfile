FROM openjdk:8-jdk-alpine
LABEL MAINTAINER="Frank Callaly"
LABEL VERSION="0.0.1"

WORKDIR /app

COPY target/rest-shorturl-0.0.1-SNAPSHOT.jar .
COPY deploy/startapp.sh .

EXPOSE 8080

ENV SPRING_PROFILE prod

RUN apk add --no-cache bash
ENTRYPOINT ["/bin/bash", "startapp.sh"]
