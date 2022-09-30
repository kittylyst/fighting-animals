FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
# RUN microdnf install net-tools
COPY animals-demo-1.0-SNAPSHOT-jar-with-dependencies.jar  /app
COPY opentelemetry-javaagent.jar  /app
WORKDIR /app
# -Dotel.metrics.exporter=logging
CMD ["java", "-javaagent:opentelemetry-javaagent.jar", "-cp" , "animals-demo-1.0-SNAPSHOT-jar-with-dependencies.jar", "-Dotel.exporter.otlp.endpoint=http://otel-collector:4317/", "-Dotel.service.name=animal-app", "io.opentelemetry.examples.animal.AnimalApplication"]
