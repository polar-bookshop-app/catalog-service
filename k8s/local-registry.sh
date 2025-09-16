#!/usr/bin/env bash

# exit immediately if a command returns a non-zero status.
set -e

docker run -d --rm -p 5000:5000 --name local-registry registry:2
