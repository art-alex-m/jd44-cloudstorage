include .env
export

DOCKER_IMAGE_NAME:=artalexm/cloudstorage

clean:
	./mvnw clean

test-only:
	./mvnw test

build:
	./mvnw install

build-fast:
	./mvnw clean package -Dmaven.test.skip

docker:
	docker build \
        --build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
        --build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
        --build-arg HOSTGROUP=${HOSTGROUP} \
        --build-arg HOSTUSER=${HOSTUSER} \
        --tag ${DOCKER_IMAGE_NAME}:latest \
		--tag ${DOCKER_IMAGE_NAME}:${WEBAPP_VERSION} \
		./webapp

run:
	${JAVA_HOME}/bin/java -jar ./webapp/target/cloudstorage-webapp-${WEBAPP_VERSION}.jar
