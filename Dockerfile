FROM openjdk:17-jdk-alpine
RUN apk add --no-cache maven
WORKDIR /usr/src/app
COPY . .
RUN mvn package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /usr/src/app
COPY --from=0 /usr/src/app/target/DBtest-1.0.jar .
RUN mkdir -p /tmpProject
RUN chmod -R 777 /tmpProject
RUN adduser -S appuser
USER appuser