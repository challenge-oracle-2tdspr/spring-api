FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="agrotech-team" \
      description="AgroTech API - Spring Boot Production" \
      version="1.0"

RUN apk add --no-cache wget ca-certificates tzdata

ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN addgroup -S agrotech && \
    adduser -S agrotech -G agrotech

WORKDIR /opt/agrotech

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R agrotech:agrotech /opt/agrotech

USER agrotech

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=prod", "-jar", "app.jar"]