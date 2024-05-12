FROM openjdk:17-alpine

LABEL maintainer=artalexm

WORKDIR /app

ARG HOSTGROUP
ARG HOSTUSER
ARG WEBAPP_VERSION
ARG DOCKER_IMAGE_EXPOSE

ADD target/cloudstorage-webapp-$WEBAPP_VERSION.jar webapp.jar

RUN addgroup -g "${HOSTGROUP}" cloudstorage &&  \
    adduser -DHs /bin/bash -G cloudstorage -u "${HOSTUSER}" azz
RUN mkdir "logs"

RUN chown azz:cloudstorage /app -R

ENV SERVER_PORT=$DOCKER_IMAGE_EXPOSE
EXPOSE $DOCKER_IMAGE_EXPOSE

USER azz
CMD ["java", "-jar", "webapp.jar"]