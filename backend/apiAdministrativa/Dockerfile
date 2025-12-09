# File: `apiAdministrativa/Dockerfile`
# language: dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY backend/pom.xml ./backend/pom.xml
COPY backend/utils ./backend/utils

# Construir e instalar utils en el repositorio local
RUN mvn -B -f backend/utils/pom.xml clean install -DskipTests

COPY backend/apiAdministrativa/pom.xml ./backend/apiAdministrativa/pom.xml

RUN mvn -B -f backend/apiAdministrativa/pom.xml dependency:go-offline

COPY backend/apiAdministrativa/src ./backend/apiAdministrativa/src

RUN mvn -B -f backend/apiAdministrativa/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

COPY --from=build /workspace/backend/apiAdministrativa/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","app.jar"]


