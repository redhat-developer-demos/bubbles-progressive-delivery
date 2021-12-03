#!/bin/bash

echo "###### Deploying FrontEnd ######"

kubectl apply -f ../../bubbles-frontend/src/main/argocd/application-kustomize-serverless.yaml

echo "###### Deploying BackEnd ######"

kubectl apply -f ../../bubbles-backend/src/main/argocd/application-kustomize-serverless.yaml