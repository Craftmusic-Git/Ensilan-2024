FROM openjdk:21-jdk-slim

LABEL org.opencontainers.image.title="Ensilan Back"

WORKDIR /app

COPY build/libs/*-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080

ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=ensilan
ENV DB_USER=postgres
ENV DB_PASSWORD=root
ENV API_KEY=secret