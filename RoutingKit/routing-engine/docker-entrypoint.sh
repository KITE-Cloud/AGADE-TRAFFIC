#!/bin/bash -eu
ELAPSED=0
echo "Routing engine waiting for neo4j to startup"
while true; do
  if curl -s --head  --request GET http://neo4j:7474/ | grep "200 OK" > /dev/null; then
    echo "Neo4j is ready. startup routing engine"
    exec java -jar "/usr/src/app/routing-engine/target/routing-engine.jar"
    exit 0
  else
    sleep 3
    continue
  fi
done


