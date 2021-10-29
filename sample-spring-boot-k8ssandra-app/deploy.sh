#!/usr/bin/env bash
APP_NAME=k8s
IMAGE_NAME=gcr.io/pgtm-jlong/${APP_NAME}
IMAGE_TAG=latest
mvn -DskipTests=true clean  spring-boot:build-image
image_id=$(docker images -q $APP_NAME)
docker tag "${image_id}" ${IMAGE_NAME}:${IMAGE_TAG}
docker push $IMAGE_NAME:${IMAGE_TAG}
kubectl delete -f deployment.yaml
kubectl apply -f deployment.yaml
