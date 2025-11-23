
FROM node:20-alpine AS ui-build

WORKDIR /app/ui

COPY UI/package*.json ./
RUN npm install

COPY UI/ .
RUN npm run build


FROM maven:3.9-eclipse-temurin-17 AS api-build

WORKDIR /app/api

COPY API/pom.xml .
COPY API/src ./src


COPY --from=ui-build /app/ui/dist ./src/main/resources/static

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=api-build /app/api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
