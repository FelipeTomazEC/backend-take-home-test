FROM gradle:7.6.5-jdk11-alpine AS builder

WORKDIR /source
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build -x test

FROM amazoncorretto:11-alpine3.21

WORKDIR /usr/src/app
COPY --from=builder /source/build/libs/sleep-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sleep-0.0.1-SNAPSHOT.jar"]
