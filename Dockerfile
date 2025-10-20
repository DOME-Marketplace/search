#FROM openjdk:11-slim

#COPY ./target/search-*.jar /usr/app/search.jar
#WORKDIR /usr/app

#RUN apt-get update && apt-get install -y \
#	curl \
#	wget
	
#ENTRYPOINT exec java $JAVA_OPTS -jar search.jar

# Use Java base image JDK 17
FROM openjdk:17-jdk-alpine

# Install curl
RUN apk update && apk add --no-cache curl

# Set the workdir in the container
WORKDIR /usr/app

# Copy JAR in the working directory
COPY target/search.jar search.jar

# Espose port 8080
EXPOSE 8080

ENV JAVA_OPTS="--add-opens=java.base/java.net=ALL-UNNAMED"

# Comand to run the Spring Boot application
#ENTRYPOINT ["java","-jar","search.jar"]
ENTRYPOINT exec java $JAVA_OPTS -jar search.jar
