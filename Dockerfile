
# Stage 1: Build frontend
FROM oven/bun:1-alpine AS web-builder
WORKDIR /web
COPY web/package.json ./
RUN bun install
COPY web/ .
RUN bun run build

# Stage 2: Build backend
FROM maven:3.9.6-eclipse-temurin-21 AS java-builder
WORKDIR /app
COPY pom.xml .
# Baixa as dependências do Maven primeiro para fazer cache dessa camada no Docker!
RUN mvn dependency:go-offline -B
COPY .mvn .mvn
COPY mvnw .
COPY src src
RUN mvn clean package -DskipTests

# Stage 3: Runtime - Nginx (port 8080) + Spring Boot (port 8081)
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache nginx supervisor

# Frontend static files
COPY --from=web-builder /web/dist /web/dist

# Backend jar
COPY --from=java-builder /app/target/*.jar /app/app.jar

# Nginx site config (remove default if present)
RUN rm -f /etc/nginx/http.d/default.conf
COPY nginx.conf /etc/nginx/http.d/default.conf

# Supervisor config
COPY supervisord.conf /etc/supervisord.conf

EXPOSE 8080
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]