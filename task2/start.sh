#!/bin/bash

docker-compose down

sleep 5s

ip=$(dig +short myip.opendns.com @resolver1.opendns.com)
#ip=$(dig -4 TXT +short o-o.myaddr.l.google.com @ns1.google.com)
#dig +short myip.opendns.com @resolver1.opendns.com
#dig -4 TXT +short o-o.myaddr.l.google.com @ns1.google.com
#dig -6 TXT +short o-o.myaddr.l.google.com @ns1.google.com

echo ${ip}

EXTERNAL_IP=${ip} docker-compose up -d

sleep 5s

docker exec -i ab-kafka kafka-topics --create --topic RAW_PAYMENTS --bootstrap-server kafka:29092 --replication-factor 1 --partitions 1

sleep 5s

docker exec -i ab-kafka kafka-console-producer --topic RAW_PAYMENTS --broker-list kafka:29092 --property parse.key=true --property key.separator=":" < testdata.txt