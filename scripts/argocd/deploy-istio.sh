#!/bin/bash

echo "###### Deploying FrontEnd ######"

kubectl apply -f ../../bubbles-frontend/src/main/argocd/application-kustomize-istio.yaml

echo "###### Deploying BackEnd ######"

kubectl apply -f ../../bubbles-backend/src/main/argocd/application-kustomize-istio.yaml

kubectl get routes -n istio-system

echo "/bubble/index.html"