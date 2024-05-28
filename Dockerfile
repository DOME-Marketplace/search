FROM openjdk:11-slim

COPY ./target/search-*.jar /usr/app/search.jar
WORKDIR /usr/app

RUN apt-get update && apt-get install -y \
	curl \
	wget
	
ENTRYPOINT exec java $JAVA_OPTS -jar search.jar
