#!/bin/bash


cd /opt/services/api-photomap
docker stop photomap-api
docker rm photomap-api
docker build -t photomap-api .
docker run -d -e 'environment.name=dev' -v '/etc/photomap:/etc/photomap' -v '/opt/data/pictures:/opt/data/pictures' -v '/opt/data/thumbs:/opt/data/thumbs' -v '/var/log/photomap-api:/var/log/photomap-api' --name="photomap-api" -p 8280:8080 -p 9990:9990 -p 1898:1898 -p 62911:62911 photomap-api