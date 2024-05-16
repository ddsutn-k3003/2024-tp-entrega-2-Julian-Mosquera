FROM openjdk:21
VOLUME /tmp
EXPOSE 8080
ADD ./target/my-app-name-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]