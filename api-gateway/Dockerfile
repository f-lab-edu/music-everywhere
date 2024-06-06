# 빌드 환경 구성
FROM amazoncorretto:21
WORKDIR /workspace/app

# JAR 파일 복사
COPY ./build/libs/api-gateway-0.0.1-SNAPSHOT.jar /workspace/app/api-gateway.jar
COPY ./src/main/resources/application.yml /workspace/app
COPY ./src/main/resources/application-route.yml /workspace/app

ENTRYPOINT ["java", "-jar", "api-gateway.jar", "--spring.config.location=file:/workspace/app/application.yml"]
