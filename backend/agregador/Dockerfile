FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY utils ./utils
COPY agregador/pom.xml ./agregador/pom.xml

RUN mvn dependency:go-offline -f agregador/pom.xml

COPY agregador/src ./agregador/src

RUN mvn clean package -f agregador/pom.xml -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/agregador/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
