#!/usr/bin/env bash

# exit immediately if a command returns a non-zero status.
set -e

kubectl port-forward service/catalog-service 9001:9001
