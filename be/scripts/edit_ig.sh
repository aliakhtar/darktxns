#!/usr/bin/env bash

export KOPS_STATE_STORE=s3://darktxns-state-store

kops edit ig nodes
kops update cluster k8s.darktxns.com --yes
kops rolling-update cluster k8s.darktxns.com --yes
