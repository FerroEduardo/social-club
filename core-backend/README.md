# Backend Core

# Main technologies

- Spring Boot (3.2.0-M2)
- OAuth2 Login (Google,GitHub)

## Requirements

- Java 17+
- PostgresSQL
- Redis

## Project setup

1. Setup PostgresSQL database [tables](/database/tables.sql) and a Redis instance
0. Setup environment variables
    - Use Intellij Run Configurations to inject or manually with Gradle CLI
    - Available variables can be found at [resources/application.yml](/resources/application.yml)
0. Setup Google and GitHub OAuth2 configurations

> Free PostgresSQL instances: [ElephantSQL](https://www.elephantsql.com/)

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