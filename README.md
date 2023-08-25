# Animal vs. Animal

## Description

This project simulates a battle between two animals chosen from several different clades of animal. Call `GET /battle` to get a battle that looks like this:

Output:

{
"good": <animal1>,
"evil": <animal1>
}

Each low-level service contains a simple `GET /getAnimal` route, and top-level service calls one of the low-level services
application's `GET /getAnimal` route for each side (chosen randomly).

The routes are as follows:

On this branch, only automatic OTel tracing via the Java agent is supported.

## Building the project

To build the project, use:

```shell
mvn clean package
```

This will generate a shaded JAR that can be picked up by the following steps.

For the OTel agent, it will need to be manually copied into the `target/` directory, like this:

```shell
cp opentelemetry-javaagent.jar target/
```

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

In the `deploy/` directory are a docker-compose YAML file and a collector config.

```shell
docker-compose up
```

The entrypoint service can then be hit on `:8080`

The Jaeger instance is available on `:16686`

## Known Issues
