#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/app/deploy
cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

CURRENT_PID=$(sudo lsof -t -i:8080)

if [ -z "$CURRENT_PID" ]; then
  echo "> no server, nothing to kill."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> deploy $JAR_PATH"
nohup java -jar $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
