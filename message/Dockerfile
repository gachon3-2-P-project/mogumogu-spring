FROM openjdk:17-jdk
# 베이스 이미지, 빌드 할 이미지가 어떤 이미지를 기반으로 하고 있는지
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# 파일을 이미지에 추가하는 것을 의미
ENTRYPOINT ["java", "-Dspring.profiles.active=dev","-jar","/app.jar"]
# 컨테이너가 생성, 시작될 때 실행되는 명령어