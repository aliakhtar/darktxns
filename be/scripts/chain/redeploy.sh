#!/usr/bin/env bash

cd ../../
sbt assembly

scp -v -i ~/.ssh/Beast.pem  target/darktxns.jar ubuntu@35.166.140.248:/home/ubuntu
./scripts/chain/ssh.sh

