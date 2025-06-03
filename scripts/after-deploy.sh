#!/bin/bash
REPOSITORY=/home/ubuntu/
cd $REPOSITORY/littlebank

# 디스크 용량 확인 및 정리
USED=$(df / | grep / | awk '{ print $5 }' | sed 's/%//g')

if [ "$USED" -gt 90 ]; then
  echo "> 디스크 용량 부족 감지 ($USED%). 캐시 정리 시작..."

  echo "> Docker 시스템 캐시 삭제"
  docker system prune -af

  echo "> Gradle 캐시 삭제"
  rm -rf ~/.gradle/caches

  echo "> 캐시 정리 완료"
else
  echo "> 디스크 용량 정상 ($USED%). 캐시 정리 생략"
fi

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
