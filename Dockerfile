FROM openjdk:17-jdk-alpine 

RUN apk update && apk add --no-cache curl

WORKDIR /usr/app

COPY target/search.jar search.jar
	
ENTRYPOINT exec java $JAVA_OPTS -jar search.jar
