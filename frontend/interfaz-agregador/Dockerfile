# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY frontend/pom.xml ./frontend/pom.xml
COPY frontend/interfaz-agregador/pom.xml ./frontend/interfaz-agregador/pom.xml
COPY frontend/interfaz-agregador/src ./frontend/interfaz-agregador/src

WORKDIR /workspace/frontend/interfaz-agregador
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Copiar el JAR compilado
COPY --from=build /workspace/frontend/interfaz-agregador/target/*.jar app.jar

# Exponer puerto
EXPOSE 8094

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

