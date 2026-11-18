# To run any microservice, we need to install JRE/Java inside any system
# we need to import the base image from the docker hub, and use it in our Dockerfile
# Start with the base image containing the Java Runtime.
FROM eclipse-temurin:17-jdk
# who is going to maintain this Docker Image either person or domain name
LABEL maintainer="eazybytes.com"
#Copy the jar from the target folder to the DockerImage, and when we generate the Docker image using this Docker file, it is going to have all the dependencies in a packaged form
# Add the application's jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar
#Whenever someone is trying to generate a container from my Docker image, please execute so and so commands
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]







