REPOSITORY=/home/ubuntu/littlebank
cd $REPOSITORY

CONTAINER_NAME="littlebank-server-dev"
IMAGE_NAME="littlebank-image-dev"
SPRING_PROFILE="dev"

# ========== 이전 컨테이너 정리 ==========
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

# ========== 기존 이미지 제거 ==========
docker rmi $IMAGE_NAME || true

# ========== Docker 이미지 빌드 ==========
docker build -t $IMAGE_NAME .

# ========== 컨테이너 실행 ==========
docker run -d \
  --name $CONTAINER_NAME \
  -p 8080:8080 \
  -v /etc/localtime:/etc/localtime:ro \
  -e TZ=Asia/Seoul \
  $IMAGE_NAME \
  --spring.profiles.active=$SPRING_PROFILE