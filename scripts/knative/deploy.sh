#!/bin/bash

echo "###### Deploying FrontEnd ######"

kubectl apply -f ../../bubbles-frontend/src/main/knative/Service.yaml

kubectl wait --for=condition=available --timeout=600s deployment/bubblefrontend-00001-deployment

echo "###### Deploying BackEnd ######"

kubectl apply -f ../../bubbles-backend/src/main/knative/Service.yaml
kubectl apply -f ../../bubbles-backend/src/main/knative/Service-v2.yaml

kubectl wait --for=condition=available --timeout=600s deployment/bubblebackend-1-deployment
kubectl wait --for=condition=available --timeout=600s deployment/bubblebackend-2-deployment

echo "###### Redirecting Traffic to V1 ######"

kubectl apply -f ../../bubbles-backend/src/main/knative/Route-to-v1.yaml