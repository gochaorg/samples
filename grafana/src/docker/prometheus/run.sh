#!/bin/bash
echo run prometheus
IMG=prom/prometheus
FILE1=/home/uzer/code/samples/grafana/src/docker/prometheus/prometheus.yml
docker run --rm -it -p 9090:9090 -v $FILE1:/etc/prometheus/prometheus.yml $IMG
