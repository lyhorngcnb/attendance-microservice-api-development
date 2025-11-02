# syntax=docker/dockerfile:1
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# Install shared library first
COPY common-lib ./common-lib
RUN mvn -q -DskipTests -f common-lib/pom.xml clean install

# Build propertyservice
COPY propertyservice/pom.xml ./propertyservice/pom.xml
COPY propertyservice/src ./propertyservice/src
RUN mvn -q -DskipTests -f propertyservice/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/propertyservice/target/*.jar app.jar
EXPOSE 8084
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
