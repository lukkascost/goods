FROM openjdk:21-jdk-slim

COPY target/goods-0.0.1.jar /usr/local/lib/run.jar

RUN apt-get update \
    && apt-get install -y --no-install-recommends tzdata \
    && rm -rf /var/lib/apt/lists/*
ENV TZ=America/Fortaleza

WORKDIR /
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAM=256m", "-jar", "/usr/local/lib/run.jar"]
