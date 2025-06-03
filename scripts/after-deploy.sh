#!/bin/bash
REPOSITORY=/home/ubuntu/
cd $REPOSITORY/littlebank

# Redis 컨테이너 실행 여부 확인 함수
is_redis_running() {
  docker compose -f "$1" ps --services --filter "status=running" | grep -q "^redis$"
}

if [ "$DEPLOYMENT_GROUP_NAME" = "env-dev" ]; then
  COMPOSE_FILE="docker-compose.dev.yml"

  # Redis가 실행 중이지 않다면 redis도 재시작
  if ! is_redis_running "$COMPOSE_FILE"; then
    echo "> Redis is NOT running. Restarting redis (dev)"
    docker compose -f "$COMPOSE_FILE" rm -f redis
    docker compose -f "$COMPOSE_FILE" up --build -d redis
  fi

  # Spring Boot는 항상 재시작
  echo "> Restarting spring_boot (dev)"
  docker compose -f "$COMPOSE_FILE" rm -f spring_boot
  docker compose -f "$COMPOSE_FILE" up --build -d spring_boot

elif [ "$DEPLOYMENT_GROUP_NAME" = "env-prod" ]; then
  COMPOSE_FILE="docker-compose.prod.yml"

  if ! is_redis_running "$COMPOSE_FILE"; then
    echo "> Redis is NOT running. Restarting redis (prod)"
    docker compose -f "$COMPOSE_FILE" rm -f redis
    docker compose -f "$COMPOSE_FILE" up --build -d redis
  fi

  echo "> Restarting spring_boot (prod)"
  docker compose -f "$COMPOSE_FILE" rm -f spring_boot
  docker compose -f "$COMPOSE_FILE" up --build -d spring_boot
fi
