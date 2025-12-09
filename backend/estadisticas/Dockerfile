FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY backend/pom.xml ./backend/pom.xml
COPY backend/utils ./backend/utils

# Construir e instalar utils en el repositorio local
RUN mvn -B -f backend/utils/pom.xml clean install -DskipTests

COPY backend/estadisticas/pom.xml ./backend/estadisticas/pom.xml

RUN mvn dependency:go-offline -f backend/estadisticas/pom.xml

COPY backend/estadisticas/src ./backend/estadisticas/src

RUN mvn clean package -f backend/estadisticas/pom.xml -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

COPY --from=build /workspace/backend/estadisticas/target/*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
