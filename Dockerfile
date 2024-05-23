FROM openjdk:11-slim

COPY ./target/search-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app

RUN apt-get update && apt-get install -y \
	curl \
	wget
	
ENTRYPOINT exec java $JAVA_OPTS -jar search-0.0.1-SNAPSHOT.jar
