#!/bin/bash

# Function to check if Cassandra is ready
function wait_for_cassandra() {
  until echo "SELECT now() FROM system.local" | cqlsh -u cassandra -p cassandra; do
    echo "Waiting for Cassandra to be available..."
    sleep 5
  done
  echo "Cassandra is up and running."
}

# Wait for Cassandra to be available
wait_for_cassandra

# Execute the CQL script to create the keyspace
cqlsh -u cassandra -p cassandra -f /create-keyspace.cql
