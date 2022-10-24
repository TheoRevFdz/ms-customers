FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/ms-customers-0.0.1-SNAPSHOT.jar ./ms-customers.jar

EXPOSE 8081

CMD [ "java", "-jar", "ms-customers.jar" ]