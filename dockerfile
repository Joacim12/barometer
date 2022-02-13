#https://codefresh.io/docker-tutorial/create-docker-images-for-java/
# the first stage of our build will use a maven 3.6.1 parent image
FROM maven:3.8.3-openjdk-17 AS MAVEN_BUILD
# Change directory to the backend directory
# copy the pom and src code to the container
COPY demo/api/. .
# package our application code
RUN mvn clean package

# the second stage of our build will use open jdk 17 on oraclelinux 8
FROM openjdk:17-jdk-oraclelinux8
# Change directory to the backend directory
# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/api-1.0.jar /app.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/app.jar"]