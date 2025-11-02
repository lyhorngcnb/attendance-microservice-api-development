# syntax=docker/dockerfile:1
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# Install shared library first
COPY common-lib ./common-lib
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests -f common-lib/pom.xml clean install \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.pool=false

# Build api-gateway
COPY api-gateway/pom.xml ./api-gateway/pom.xml
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests -f api-gateway/pom.xml dependency:go-offline \
    -Dmaven.wagon.http.retryHandler.count=5
COPY api-gateway/src ./api-gateway/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests -f api-gateway/pom.xml package \
    -Dmaven.wagon.http.retryHandler.count=5

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
