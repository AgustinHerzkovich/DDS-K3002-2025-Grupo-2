# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY backend/pom.xml ./backend/pom.xml
COPY backend/utils ./backend/utils

# Construir e instalar utils en el repositorio local
RUN mvn -B -f backend/utils/pom.xml clean install -DskipTests

COPY backend/EurekaServer/pom.xml ./backend/EurekaServer/pom.xml
COPY backend/EurekaServer/src ./backend/EurekaServer/src

WORKDIR /workspace/backend/EurekaServer
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Copiar el JAR compilado
COPY --from=build /workspace/backend/EurekaServer/target/eurekaServer-*.jar app.jar

# Exponer puerto
EXPOSE 8761

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

