#This Docker file helps to run a animal service

#To build a package

# mvn package

#To build a image

docker build  -t animals_demo -f src/main/docker/animal/Dockerfile .
docker build  -t fish_demo -f src/main/docker/fish/Dockerfile .
docker build  -t mustelid_demo -f src/main/docker/mustelid/Dockerfile .
docker build  -t feline_demo -f src/main/docker/feline/Dockerfile .
docker build  -t mammal_demo -f src/main/docker/mammal/Dockerfile .

#List a images
