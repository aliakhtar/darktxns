#!/usr/bin/env bash

echo "Starting up!"

echo "Kafka home: $KAFKA_HOME"
echo "Broker id: $KAFKA_BROKER_ID"
echo "Log dir: $KAFKA_LOG_DIR"
echo "Zookeeper connect: $KAFKA_ZOOKEEPER_CONNECT"
echo "Advertised host: $KAFKA_ADVERTISED_HOST"
echo "Advertised port: $KAFKA_ADVERTISED_PORT"

cp server.properties server.properties.bk
sed -i -r "s/\[BROKER_ID\]/$KAFKA_BROKER_ID/g" server.properties

# Log dir may have slashes, so use ~ as delimiter for sed. http://stackoverflow.com/questions/27787536/how-to-pass-a-variable-containing-slashes-to-sed
sed -i -r "s~\[LOG_DIR\]~$KAFKA_LOG_DIR~g" server.properties
sed -i -r "s/\[ZOOKEEPER_CONNECT\]/$KAFKA_ZOOKEEPER_CONNECT/g" server.properties
sed -i -r "s/\[ADVERTISED_HOST\]/$KAFKA_ADVERTISED_HOST/g" server.properties
sed -i -r "s/\[ADVERTISED_PORT\]/$KAFKA_ADVERTISED_PORT/g" server.properties

##

KAFKA_PID=0

# see https://medium.com/@gchudnov/trapping-signals-in-docker-containers-7a57fdda7d86#.bh35ir4u5
term_handler() {
  echo 'Stopping Kafka....'
  if [ $KAFKA_PID -ne 0 ]; then
    kill -s TERM "$KAFKA_PID"
    wait "$KAFKA_PID"
  fi
  echo 'Kafka stopped.'
  exit
}


# Capture kill requests to stop properly
trap "term_handler" SIGHUP SIGINT SIGTERM


mv server.properties $KAFKA_HOME/config/server.properties

echo "server.properties:"
cat $KAFKA_HOME/config/server.properties

$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties &
KAFKA_PID=$!

wait