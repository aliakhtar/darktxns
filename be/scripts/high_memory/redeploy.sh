#!/usr/bin/env bash

cd ../../
sbt assembly

scp -v -i ~/.ssh/Beast.pem  target/darktxns.jar ubuntu@35.166.185.202:/home/ubuntu
./scripts/high_memory/ssh.sh

