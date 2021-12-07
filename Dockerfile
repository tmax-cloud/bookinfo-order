FROM docker.io/openjdk:11-jdk
LABEL PROJECT_NAME=bookInfo \
      PROJECT=bookInfo

EXPOSE 8078 8078
ENTRYPOINT ["java","-jar","/order.jar"]
