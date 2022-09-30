FROM registry.access.redhat.com/ubi8/openjdk-17:1.13-1
USER root
RUN mkdir /app
COPY animals-demo-1.0-SNAPSHOT-jar-with-dependencies.jar  /app
WORKDIR /app
CMD ["java", "-cp" , "animals-demo-1.0-SNAPSHOT-jar-with-dependencies.jar", "io.opentelemetry.examples.mammal.MammalApplication", "--server.port=8081"]

