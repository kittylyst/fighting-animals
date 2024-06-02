FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY target/animals-demo-1.0-SNAPSHOT.jar  /app
WORKDIR /app
CMD ["java", "-cp" , "animals-demo-1.0-SNAPSHOT.jar", "-Dotel.service.name=feline-service", "-Dotel.metrics.exporter=none", "-Dotel.exporter.otlp.endpoint=http://otel-collector:4317/", "-Dloader.main=io.opentelemetry.examples.feline.FelineApplication", "org.springframework.boot.loader.launch.PropertiesLauncher", "--server.port=8085"]

