# Cambié la primera línea para utilizar una imagen base que incluye Maven y JDK
FROM maven:3.8.5-openjdk-17 AS build

# Crear y establecer el directorio de trabajo
WORKDIR /app

# Copiar solo el archivo pom.xml para descargar dependencias y aprovechar la caché de Maven
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar el resto de la aplicación
COPY . .

# Construir la aplicación
RUN mvn clean package -DskipTests

# Imagen base para ejecutar la aplicación
FROM openjdk:17.0.1-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/ISO2023-0.0.1-SNAPSHOT.jar ISO2023.jar

# Exponer el puerto
EXPOSE 8080

# Comando de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "ISO2023.jar"]
