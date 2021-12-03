#!/bin/bash

echo "###### Redirecting Traffic to V2 ######"

kubectl apply -f ../../bubbles-backend/src/main/knative/Route-to-v1.yaml