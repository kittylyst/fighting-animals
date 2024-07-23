# To build the Maven jar
mvn clean package -Dskip.spotless=true

# Build the containers
podman build  -t animals_demo -f src/main/docker/animal/Dockerfile .
podman build  -t fish_demo -f src/main/docker/fish/Dockerfile .
podman build  -t mustelid_demo -f src/main/docker/mustelid/Dockerfile .
podman build  -t feline_demo -f src/main/docker/feline/Dockerfile .
podman build  -t mammal_demo -f src/main/docker/mammal/Dockerfile .
