#!/usr/bin/env bash

# exit immediately if a command returns a non-zero status.
set -e

kubectl apply -f catalog-db.yml -f catalog-service.yml
