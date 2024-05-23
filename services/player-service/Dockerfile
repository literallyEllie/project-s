FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY server/build/libs/player-service.jar /app/player-service.jar
CMD ["java", "-jar", "/app/player-service.jar"]
