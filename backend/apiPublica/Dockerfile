

# File: `apiPublica/Dockerfile`
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY utils ./utils
COPY apiPublica/pom.xml ./apiPublica/pom.xml

RUN mvn -B -f apiPublica/pom.xml dependency:go-offline

COPY apiPublica/src ./apiPublica/src

RUN mvn -B -f apiPublica/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/apiPublica/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]