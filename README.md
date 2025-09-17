# catalog-service

Catalog Service Spring Boot REST API application.

## Build and Run locally

* Build application without tests

```bash
./gradlew build -i -x test
```

* Start Postgres DB in docker (detached mode)

```bash
./db/start-db.sh
```

* Start service locally

```bash
./scripts/run.sh
```

## Docker image

* Create catalog-service docker image using Cloud Native Buildpacks

```bash
./gradlew bootBuildImage
```

* Start Postgres DB in docker (detached mode)

```bash
./db/start-db.sh
```

## Vulnerability Scan

We will use [grype](https://github.com/anchore/grype) as our vulnerability scanner for CI and locally.

* Scan local repository

```bash
grype . --name catalog-service
```

* Scan docker image

```bash
grype docker:catalog-service:latest
```

## K8S and Local Registry

* For K8S manifest validation use [kubeconform](https://github.com/yannh/kubeconform)

```bash
kubeconform -strict -summary k8s/catalog-db.yml k8s/catalog-service.yml
```

* Build, publish and deploy to local K8S cluster

```bash
./k8s/deploy-all.sh
```

If for some reason port-forwarding failed just execute:

```bash
kubectl port-forward service/catalog-service 9001:9001 8001:8001
```

* Clean local K8S cluster

```bash
./k8s/remove-all.sh
```

## Logo

Logo generated using https://patorjk.com/software/taag/ and `Standard` type.

## References

* Initial template created
  using [start.spring.io](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=3.5.5&packaging=jar&jvmVersion=24&groupId=com.github.polar&artifactId=catalog-service&name=catalog-service&description=Catalog%20Service%20Spring%20Boot%20API&packageName=com.github.polar.catalog-service&dependencies=web,postgresql,testcontainers,devtools,data-jdbc,flyway)
