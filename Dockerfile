FROM openjdk:18.0.2.1-slim-buster

COPY ./ddproject-app/target/ddproject-app.jar ./
CMD java -jar /ddproject-app.jar