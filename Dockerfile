FROM gradle:jdk21-graal AS BUILD
WORKDIR /usr/app/
COPY . .

RUN gradle build
FROM openjdk:17-jdk-slim
COPY --from=BUILD /usr/app .
EXPOSE 8080
ENTRYPOINT exec java -jar build/libs/api-security-1.0.0.jar