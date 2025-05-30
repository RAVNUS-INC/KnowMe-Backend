FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=./build/libs/FishShip-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

# Use the JAR file generated in target directory (adjust filename if needed)
ENTRYPOINT ["java", "-jar", "app.jar"]