#!/bin/bash

kubectl delete -f ../../bubbles-frontend/src/main/knative/Service.yaml

kubectl delete -f ../../bubbles-backend/src/main/knative/Service.yaml

kubectl delete -f ../../bubbles-backend/src/main/knative/Route-to-v1.yaml