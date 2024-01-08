FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY target/animals-demo-1.0-SNAPSHOT.jar  /app
COPY opentelemetry-javaagent.jar  /app
WORKDIR /app
CMD ["java", "-javaagent:opentelemetry-javaagent.jar", "-cp" , "animals-demo-1.0-SNAPSHOT.jar", "-Dotel.traces.exporter=none", "-Dotel.exporter.otlp.endpoint=http://otel-collector:4317/", "-Dotel.service.name=feline-app", "-Dloader.main=io.opentelemetry.examples.feline.FelineApplication", "org.springframework.boot.loader.launch.PropertiesLauncher", "--server.port=8085"]

