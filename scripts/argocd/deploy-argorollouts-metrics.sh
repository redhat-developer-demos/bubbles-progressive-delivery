#!/bin/bash

echo "###### Deploying FrontEnd ######"

kubectl apply -f ../../bubbles-frontend/src/main/argocd/application-kustomize-argorollouts.yaml

echo "###### Deploying BackEnd ######"

kubectl apply -f ../../bubbles-backend/src/main/argocd/application-kustomize-argorollouts-metrics.yaml

kubectl get routes -n istio-system

echo "/index.html"