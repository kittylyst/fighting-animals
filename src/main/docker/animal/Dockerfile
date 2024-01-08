FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY target/animals-demo-1.0-SNAPSHOT.jar  /app
COPY opentelemetry-javaagent.jar  /app
WORKDIR /app
# -Dmanagement.otlp.metrics.exporter.endpoint=http://otel-collector:4317/
# -Dotel.exporter.otlp.endpoint=http://otel-collector:4317/
CMD ["java", "-javaagent:opentelemetry-javaagent.jar", "-Dotel.traces.exporter=none", "-Dmanagement.otlp.metrics.exporter.endpoint=http://otel-collector:4317/", "-Dotel.service.name=animal-app", "-jar" , "animals-demo-1.0-SNAPSHOT.jar"]
