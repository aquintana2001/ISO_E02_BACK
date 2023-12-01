FROM openjdk:17-jre-slim
WORKDIR /ISO2023
COPY ./pom.xml .
COPY . .
RUN apt-get update && apt-get install -y maven
RUN mvn clean install
EXPOSE 8080
CMD ["java", "-jar", "target/ISO2023.jar"]
