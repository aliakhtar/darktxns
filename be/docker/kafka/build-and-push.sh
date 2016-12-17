#!/usr/bin/env bash

echo "Building docker image.."

docker build -t aliakhtar/kafka .

echo "Pushing image to docker.."
docker tag aliakhtar/kafka:latest aliakhtar/kafka/:latest
docker push aliakhtar/kafka:latest
