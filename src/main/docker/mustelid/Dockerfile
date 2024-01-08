FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY target/animals-demo-1.0-SNAPSHOT.jar  /app
COPY opentelemetry-javaagent.jar  /app
WORKDIR /app
# -Dotel.exporter.otlp.endpoint.url=http://otel-collector:4317/
# -Dotel.exporter.otlp.endpoint=http://otel-collector:4317/
# -Dmanagement.otlp.metrics.export.url=http://otel-collector:4317/
CMD ["java", "-javaagent:opentelemetry-javaagent.jar", "-cp" , "animals-demo-1.0-SNAPSHOT.jar", "-Dotel.traces.exporter=none", "-Dmanagement.otlp.metrics.export.url=http://otel-collector:4317/", "-Dotel.service.name=mustelid-app", "-Dloader.main=io.opentelemetry.examples.mustelid.MustelidApplication", "org.springframework.boot.loader.launch.PropertiesLauncher", "--server.port=8084"]

