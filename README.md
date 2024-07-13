# Animal vs. Animal

## Description

This project provides a simple structure to demonstrate Observability (especially distributed traces).

There are several branches:

* main - no Observability
* micrometer_only - Micrometer Metrics only
* manual_tracing - OTel Tracing using manual spans
* auto_tracing_only - Use of the OTel Java agent to trace automatically
* logging_only - 
* auto_otel - 

The system of microservices simulates a battle between two animals chosen from several different clades of animal. Call `GET /battle` to get a battle that looks like this:

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
mvn clean package
```

This will generate a shaded JAR that can be picked up by the following steps.

The project is deployed using Docker. Each separate subcomponent needs a separate container, they are built like this:

```
docker build  -t animals_demo -f src/main/docker/animal/Dockerfile .
docker build  -t fish_demo -f src/main/docker/fish/Dockerfile .
docker build  -t mustelid_demo -f src/main/docker/mustelid/Dockerfile .
docker build  -t feline_demo -f src/main/docker/feline/Dockerfile .
docker build  -t mammal_demo -f src/main/docker/mammal/Dockerfile .
```

That is, the tag name should match the contents of `docker-compose.yml`

## Running the project

In the deploy directory are a docker-compose YAML file and a collector config.

```shell
docker-compose up
```

On this branch, no Observability components are deployed.

## Known Issues
