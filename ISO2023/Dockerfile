FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /ISO2023
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
WORKDIR /ISO2023
COPY --from=build /target/ISO2023-0.0.1-SNAPSHOT.jar ISO2023.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ISO2023.jar"]
