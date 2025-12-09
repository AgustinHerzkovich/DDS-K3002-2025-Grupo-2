FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY estadisticas/pom.xml ./estadisticas/pom.xml
COPY utils ./utils

RUN mvn dependency:go-offline -f estadisticas/pom.xml

COPY estadisticas/src ./estadisticas/src

RUN mvn clean package -f estadisticas/pom.xml -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/estadisticas/target/*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
