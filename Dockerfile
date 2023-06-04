FROM openjdk:17-jdk-alpine
COPY api/build/libs/api-0.0.1-SNAPSHOT.jar /app/api.jar
WORKDIR /app
CMD ["java", "-jar", "api.jar"]