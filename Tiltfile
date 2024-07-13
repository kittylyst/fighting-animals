local_resource(
  'monorepo-java-compile',
  'mvn package',
  deps=['src', 'pom.xml'])

docker_build(
  'animals_demo',
  '.',
  dockerfile='./src/main/docker/animal/Dockerfile')

docker_build(
  'feline_demo',
  '.',
  dockerfile='./src/main/docker/feline/Dockerfile')

docker_build(
  'fish_demo',
  '.',
  dockerfile='./src/main/docker/fish/Dockerfile')

docker_build(
  'mammal_demo',
  '.',
  dockerfile='./src/main/docker/mammal/Dockerfile')

docker_build(
  'mustelid_demo',
  '.',
  dockerfile='./src/main/docker/mustelid/Dockerfile')

docker_compose("deploy/docker-compose.yml")