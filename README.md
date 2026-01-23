# device-manager

Device management service.

## Documentation

Swagger api - http://localhost:8065/api/swagger-ui/index.html

Documentation for all endpoints and testing capabilities is available.

## Build project

```bash
 ./gradlew build
```

For application run there is a need for PostgreSQL database with `device_manager` database and `device_manager` schema

## Run PostgreSQL

In order to prepare PostgreSQL you can use docker-compose.yaml in directory ./docker-db
```bash
docker-compose up -d
```
