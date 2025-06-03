#!/bin/bash
REPOSITORY=/home/ubuntu/
cd $REPOSITORY/littlebank

# docker compose up
if [ "$DEPLOYMENT_GROUP_NAME" = "env-dev" ]; then
  echo "> Stop & Remove spring_boot container only (dev)"
  docker compose -f docker-compose.dev.yml rm -f spring_boot

  echo "> Rebuild & restart spring_boot service only (dev)"
  docker compose -f docker-compose.dev.yml up --build -d spring_boot

elif [ "$DEPLOYMENT_GROUP_NAME" = "env-prod" ]; then
  echo "> Stop & Remove spring_boot container only (prod)"
  docker compose -f docker-compose.prod.yml rm -f spring_boot

  echo "> Rebuild & restart spring_boot service only (prod)"
  docker compose -f docker-compose.prod.yml up --build -d spring_boot
fi
