#!/usr/bin/env bash

DOCKER_IMAGE_NAME=catalog-service:0.0.1-SNAPSHOT

##############################################################################################
# Run docker
#
# For build pack related parameters check https://paketo.io/docs/howto/java/
#
# Setting --memory and --memory-swap with the same value
# disable SWAP completely (https://docs.docker.com/config/containers/resource_constraints/#prevent-a-container-from-using-swap)
#
# For JVM CPU & RAM sizing check
# https://learn.microsoft.com/en-us/azure/developer/java/containers/overview
#
##############################################################################################
docker run --rm \
  -p 8080:8080 \
  -e BPL_JAVA_NMT_ENABLED=false \
  --cpus=2 \
  --memory=2g --memory-swap=2g --memory-reservation=1g \
  --name catalog-service \
  $DOCKER_IMAGE_NAME
