FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

RUN mkdir -p target/extracted && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="agrotech-team" \
      description="AgroTech API - Spring Boot Production" \
      version="1.0"

RUN apk update --no-cache && \
    apk upgrade --no-cache && \
    apk add --no-cache curl wget ca-certificates tzdata && \
    rm -rf /var/cache/apk/*

ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN addgroup -S agrotech && \
    adduser -S agrotech -G agrotech

WORKDIR /opt/agrotech

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R agrotech:agrotech /opt/agrotech && \
    chmod 500 /opt/agrotech && \
    chmod 400 /opt/agrotech/app.jar

USER agrotech

HEALTHCHECK --interval=30s \
            --timeout=3s \
            --start-period=60s \
            --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:${SERVER_PORT:-8080}/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", \
    "-XX:InitialRAMPercentage=70", \
    "-XX:MaxRAMPercentage=70", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-XX:+OptimizeStringConcat", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", \
    "app.jar"]
