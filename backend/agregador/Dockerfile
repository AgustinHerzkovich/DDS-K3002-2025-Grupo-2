FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY backend/pom.xml ./backend/pom.xml
COPY backend/utils ./backend/utils

# Construir e instalar utils en el repositorio local
RUN mvn -B -f backend/utils/pom.xml clean install -DskipTests

COPY backend/agregador/pom.xml ./backend/agregador/pom.xml

RUN mvn dependency:go-offline -f backend/agregador/pom.xml

COPY backend/agregador/src ./backend/agregador/src

RUN mvn clean package -f backend/agregador/pom.xml -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

COPY --from=build /workspace/backend/agregador/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
