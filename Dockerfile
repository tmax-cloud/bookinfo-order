FROM openjdk:11

ADD ./build/libs/order.jar /app/

WORKDIR /app

CMD ["java", "-jar", "order.jar"]