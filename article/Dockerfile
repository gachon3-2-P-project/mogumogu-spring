FROM openjdk:17-jdk as builder
# 베이스 이미지, 빌드 할 이미지가 어떤 이미지를 기반으로 하고 있는지

WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17-jdk
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
# 파일을 이미지에 추가하는 것을 의미

ENTRYPOINT ["java", "-Dspring.profiles.active=dev","org.springframework.boot.loader.JarLauncher"]
# 컨테이너가 생성, 시작될 때 실행되는 명령어