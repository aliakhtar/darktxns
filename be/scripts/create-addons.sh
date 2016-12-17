#!/usr/bin/env bash

# Creates Dashboard,  Heapster (for visualizations) addons


# Kubernetes Dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/kops/master/addons/kubernetes-dashboard/v1.4.0.yaml

# Heapster - Needed for visualizations
kubectl apply -f https://raw.githubusercontent.com/kubernetes/heapster/master/deploy/kube-config/influxdb/grafana-service.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/heapster/master/deploy/kube-config/influxdb/heapster-service.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/heapster/master/deploy/kube-config/influxdb/influxdb-service.yaml
kubectl apply -f https://gist.githubusercontent.com/aliakhtar/ceda67ada28bae0708f06bbd51a84b9c/raw/6ee75bc6c57d70afd9d9c64b89e6af07bc813926/influxdb-grafana-controller.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/heapster/master/deploy/kube-config/influxdb/heapster-deployment.yaml


