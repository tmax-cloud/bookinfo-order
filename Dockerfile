FROM docker.io/openjdk:11

ADD ./order.jar /app/

WORKDIR /app

CMD ["java", "-jar", "order.jar"]
