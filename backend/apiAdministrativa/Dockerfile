# File: `apiAdministrativa/Dockerfile`
# language: dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy root pom and shared modules
COPY pom.xml ./pom.xml
COPY utils ./utils
COPY apiAdministrativa/pom.xml ./apiAdministrativa/pom.xml

RUN mvn -B -f apiAdministrativa/pom.xml dependency:go-offline

# copy module sources (relative to context ../backend)
COPY apiAdministrativa/src ./apiAdministrativa/src

RUN mvn -B -f apiAdministrativa/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/apiAdministrativa/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","app.jar"]


