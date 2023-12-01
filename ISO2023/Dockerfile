FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY ISO2023/pom.xml ISO2023/pom.xml
COPY ISO2023/src ISO2023/src
RUN mvn -f ISO2023/pom.xml clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app/target
COPY --from=build /app/ISO2023/target/ISO2023-0.0.1-SNAPSHOT.jar ISO2023.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ISO2023.jar"]
