#!/usr/bin/env bash

# Creates a Kubernetes cluster using KOPS

# If S3 bucket errors, ensure env vars for AWS id / secret are set.

export KOPS_STATE_STORE=s3://darktxns-state-store

kops create cluster --cloud=aws --zones=us-west-2a --node-count=1 --node-size=m4.large \
 --master-size=m4.large --ssh-public-key=~/.ssh/id_rsa.pub k8s.darktxns.com

echo "Sleeping for 1 min, to wait for cluster to finish completion.."
sleep 60
kops update cluster k8s.darktxns.com --yes
