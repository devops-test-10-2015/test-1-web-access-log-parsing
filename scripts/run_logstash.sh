#!/usr/bin/bash
set -xe

logFile=$1
echo $logFile
echo "logstash -f config/logstash-parse-access.conf < test_data/20_200s_over_2_minutes.log > data/out.log"
