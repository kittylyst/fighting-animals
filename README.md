# Animal vs. Animal

## Description

This project provides a simple structure to demonstrate Observability (especially distributed traces).

There are several branches:

* `main` - no Observability
* `micrometer_only` - Micrometer Metrics only (Logging Exporter)
* `micrometer_with_prom` - Micrometer Metrics with Prometheus
* `manual_tracing` - OTel Tracing using manual spans
* `auto_tracing_only` - Use of the OTel Java agent to trace automatically
* `otel_metrics_raw_api` - OTel Metrics using the raw API
* `auto_otel` - All OTel

This document covers the `otel_metrics_raw_api` branch.

The system of microserives simulates a battle between two animals chosen from several different clades of animal. Call `GET /battle` to get a battle that looks like this:

Output:

{
"good": <animal1>,
"evil": <animal1>
}

Each low-level service contains a simple `GET /getAnimal` route, and top-level service calls one of the low-level services application's `GET /getAnimal` route for each side (chosen randomly).

The routes are as follows:


## Building the project

To build the project, use:

```shell
mvn clean spotless:apply package
```

This will generate a shaded JAR that can be picked up by the following steps.

The project is deployed using Docker. Each separate subcomponent needs a separate container, they are built like this:

```
docker build -t animals_demo -f src/main/docker/animal/Dockerfile target/
docker build -t feline_demo -f src/main/docker/feline/Dockerfile target/
docker build -t fish_demo -f src/main/docker/fish/Dockerfile target/
docker build -t mammal_demo -f src/main/docker/mammal/Dockerfile target/
docker build -t mustelid_demo -f src/main/docker/mustelid/Dockerfile target/
```

That is, the tag name should match the contents of `docker-compose.yml`

## Running the project

In the `deploy` directory is a docker-compose YAML file.

```shell
docker-compose up
```

## Known Issues

