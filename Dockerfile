FROM openjdk:17-jdk-alpine 

COPY ./target/search-*.jar /usr/app/search.jar
WORKDIR /usr/app

FROM openjdk:17-jdk-alpine 
	
ENTRYPOINT exec java $JAVA_OPTS -jar search.jar
