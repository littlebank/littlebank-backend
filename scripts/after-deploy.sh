#!/bin/bash

# ========== 기본 변수 ==========
REPOSITORY=/home/ubuntu/littlebank
cd $REPOSITORY

CONTAINER_NAME="littlebank-server"
IMAGE_NAME="littlebank-image"
DOCKER_NETWORK="backend-net"

# ========== 배포 환경 설정 ==========
SPRING_PROFILE=""

if [ "$DEPLOYMENT_GROUP_NAME" = "env-dev" ]; then
  SPRING_PROFILE="dev"
elif [ "$DEPLOYMENT_GROUP_NAME" = "env-prod" ]; then
  SPRING_PROFILE="prod"
else
  echo "[ERROR] Unknown DEPLOYMENT_GROUP_NAME: $DEPLOYMENT_GROUP_NAME"
  exit 1
fi

echo "[INFO] DEPLOYING with profile: $SPRING_PROFILE"

# ========== 이전 컨테이너 정리 ==========
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

# ========== 기존 이미지 제거 (선택) ==========
docker rmi $IMAGE_NAME || true

# ========== Docker 이미지 빌드 ==========
docker build -t $IMAGE_NAME .

# ========== 컨테이너 실행 ==========
docker run -d -p 8080:8080 \
  --name $CONTAINER_NAME \
  --network $DOCKER_NETWORK \
  -v /etc/localtime:/etc/localtime:ro \
  -e TZ=Asia/Seoul \
  $IMAGE_NAME \
  --spring.profiles.active=$SPRING_PROFILE
