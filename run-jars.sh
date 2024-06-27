#!/bin/bash


for jar in /app/*.jar; do
  if [[ "$jar" == *"cuptea-db"* ]]; then
    continue
  fi

  java -jar -Dspring.profiles.active=dev "$jar" &
done

wait