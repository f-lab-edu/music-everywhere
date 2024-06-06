# 빌드 환경 구성
FROM amazoncorretto:21
WORKDIR /workspace/app

# JAR 파일 복사
COPY ./build/libs/eureka-discovery-0.0.1-SNAPSHOT.jar /workspace/app/eureka-discovery.jar
COPY ./src/main/resources/application.yml /workspace/app

ENTRYPOINT ["java", "-jar", "eureka-discovery.jar", "--spring.config.location=file:/workspace/app/application.yml"]
