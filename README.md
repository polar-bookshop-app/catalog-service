# catalog-service

Catalog Service Spring Boot REST API application.

## Build and Run locally

```bash
./gradlew build -i
```

```bash
./scripts/run.sh
```

## Docker image

* Create catalog-service docker image using Cloud Native Buildpacks

```bash
./gradlew bootBuildImage
```

* Scan docker image for vulnerabilities (optional)

```bash
grype docker:catalog-service:0.0.1-SNAPSHOT
```

* Run docker locally

```bash
./scripts/run-docker.sh
```

## Vulnerability Scan

We will use [grype](https://github.com/anchore/grype) as our vulnerability scanner for CI and locally.

```bash
grype . --name catalog-service
```

## Logo

Logo generated using https://patorjk.com/software/taag/ and `Standard` type.

## References

* Initial template created
  using [start.spring.io](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=3.5.5&packaging=jar&jvmVersion=24&groupId=com.github.polar&artifactId=catalog-service&name=catalog-service&description=Catalog%20Service%20Spring%20Boot%20API&packageName=com.github.polar.catalog-service&dependencies=web,postgresql,testcontainers,devtools,data-jdbc,flyway)
