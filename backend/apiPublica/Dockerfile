

# File: `apiPublica/Dockerfile`
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY backend/pom.xml ./backend/pom.xml
COPY backend/utils ./backend/utils

# Construir e instalar utils en el repositorio local
RUN mvn -B -f backend/utils/pom.xml clean install -DskipTests

COPY backend/apiPublica/pom.xml ./backend/apiPublica/pom.xml

RUN mvn -B -f backend/apiPublica/pom.xml dependency:go-offline

COPY backend/apiPublica/src ./backend/apiPublica/src

RUN mvn -B -f backend/apiPublica/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

COPY --from=build /workspace/backend/apiPublica/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]