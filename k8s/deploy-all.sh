#!/usr/bin/env bash

# exit immediately if a command returns a non-zero status.
set -e

echo "Starting local docker registry"
docker stop local-registry 2>/dev/null || true
docker run -d --rm -p 5000:5000 --name local-registry registry:2

echo "Building and pushing docker image 'localhost:5000/catalog-service:latest'"
./gradlew bootBuildImage --imageName localhost:5000/catalog-service:latest --quiet

docker push localhost:5000/catalog-service:latest

echo "Deploying to local K8S"
kubectl apply -f k8s/catalog-db.yml -f k8s/catalog-service.yml

echo "Waiting for 'catalog-db' pod to be READY for 60 seconds"
kubectl wait --for=condition=ready pod -l app=catalog-db --timeout=60s

echo "Waiting for 'catalog-service' pod to be READY for 60 seconds"
kubectl wait --for=condition=ready pod -l app=catalog-service --timeout=60s

echo "Port forwarding for 'service/catalog-service 9001:9001 8001:8001'"
kubectl port-forward service/catalog-service 9001:9001 8001:8001


