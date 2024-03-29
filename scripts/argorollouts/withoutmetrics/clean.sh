#!/bin/bash

kubectl delete -f ../../bubbles-backend/src/main/argorollouts/rollout.yaml

kubectl delete -f ../../bubbles-backend/src/main/argorollouts/Service.yaml
kubectl delete -f ../../bubbles-backend/src/main/argorollouts/Service-canary.yaml
kubectl delete -f ../../bubbles-backend/src/main/argorollouts/virtual-service-bubble-backend-v1_and_v2_100_0.yaml


kubectl delete -f ../../bubbles-frontend/src/main/argorollouts/Gateway.yaml

kubectl delete -f ../../bubbles-frontend/src/main/argorollouts/Deployment.yaml
kubectl delete -f ../../bubbles-frontend/src/main/argorollouts/Service.yaml


