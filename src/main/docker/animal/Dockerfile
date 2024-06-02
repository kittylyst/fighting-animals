FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY target/animals-demo-1.0-SNAPSHOT.jar  /app
WORKDIR /app
CMD ["java", "-Dotel.service.name=animal-service", "-Dotel.exporter.otlp.endpoint=http://otel-collector:4317/", "-jar" , "animals-demo-1.0-SNAPSHOT.jar"]

