# 빌드 환경 구성
FROM amazoncorretto:21
WORKDIR /workspace/app

# JAR 파일 복사
COPY ./build/libs/group-service-0.0.1-SNAPSHOT.jar /workspace/app/group-service.jar
COPY ./src/main/resources/application.yml /workspace/app
COPY ./src/main/resources/application-route.yml /workspace/app

ENTRYPOINT ["java", "-jar", "group-service.jar", "--spring.config.location=file:/workspace/app/application.yml"]
