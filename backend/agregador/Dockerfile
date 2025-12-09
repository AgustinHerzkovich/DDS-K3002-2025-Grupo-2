# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copiar POM padre
COPY ../pom.xml ./pom.xml

# Copiar módulo utils si existe (dependencia común)
COPY ../utils ./utils

# Copiar módulo agregador
COPY ./pom.xml ./agregador/pom.xml
COPY ./src ./agregador/src

# Compilar desde el directorio raíz para resolver dependencias
WORKDIR /workspace/agregador
RUN mvn clean package -DskipTests -pl . -am

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Copiar el JAR compilado
COPY --from=build /workspace/agregador/target/*.jar app.jar

# Exponer puerto
EXPOSE 8084

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

