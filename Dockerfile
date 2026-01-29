# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de config de maven
COPY pom.xml .
# Descargar dependencias
RUN mvn dependency:go-offline -B

# Copiar el cod fuente
COPY src ./src

# Compilar la app
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear directorio para la base de datos
RUN mkdir -p /app/data

# Copiar el jar desde el stage de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la app
EXPOSE 8080

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
