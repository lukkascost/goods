FROM openjdk:17-alpine

COPY target/goods-0.0.1.jar /usr/local/lib/run.jar

RUN apk add --no-cache tzdata
ENV TZ America/Fortaleza

WORKDIR /
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAM=256m", "-jar", "/usr/local/lib/run.jar"]