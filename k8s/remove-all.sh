#!/usr/bin/env bash

# exit immediately if a command returns a non-zero status.
set -e

echo "Stopping local docker registry"
docker stop local-registry 2>/dev/null || true

echo "Removing resources from local K8S"
kubectl delete -f k8s/catalog-db.yml -f k8s/catalog-service.yml
