#!/usr/bin/env bash

cd ../
sbt assembly

scp -v -i ~/.ssh/Beast.pem  target/darktxns.jar ubuntu@35.161.174.81:/home/ubuntu
./scripts/ssh.sh

