#This make file builds the docker images locally

#To build a package
mvn clean package

# Get the Otel agent jar
wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar && mv opentelemetry-javaagent.jar target/

#To build a image
docker build -t animals_demo -f src/main/docker/animal/Dockerfile target/
docker build -t feline_demo -f src/main/docker/feline/Dockerfile target/
docker build -t fish_demo -f src/main/docker/fish/Dockerfile target/
docker build -t mammal_demo -f src/main/docker/mammal/Dockerfile target/
docker build -t mustelid_demo -f src/main/docker/mustelid/Dockerfile target/

#List a images
docker images "*demo"

