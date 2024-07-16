FROM amazoncorretto:17-alpine-jdk

COPY target/ms-gestion-trabajo-grado-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app.jar"]