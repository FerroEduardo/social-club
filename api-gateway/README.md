# API Gateway

# Main technologies

- Spring Cloud Gateway

## Requirements

- Java 17+
- Redis

## Project setup

1. Setup a Redis instance
1. Setup environment variables
    - Use Intellij Run Configurations to inject or manually with Gradle CLI
    - Available variables can be found at [resources/application.yml](src/main/resources/application.yml)

> Free Redis instances: [Redis](https://redis.com/)

### Build

```sh
./gradlew build
```

### Run development environment

```sh
./gradlew bootRun
```

### Generate JAR

```sh
./gradlew bootJar
```

> JAR is generated at `build/libs/`

### Run JAR

```sh
java -jar generated_file.jar
```