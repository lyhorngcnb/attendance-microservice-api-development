# Microservice API Development - Dockerized Setup and Gateway Guide

## Overview
- **Services**
  - api-gateway (Spring Cloud Gateway) on port 8080
  - auth-service on port 8082
  - user-service on port 8081
  - customerservice on port 8083
  - propertyservice on port 8084
  - elasticsearch on port 9200 (single-node, dev only)

## Run with Docker
- **Build and start**
  - `docker compose up --build`
- **Stop**
  - `docker compose down`

## API Gateway Profiles
- **Development**: `application-dev.yml` points to localhost services
- **Production/Compose**: `application-pro.yml` points to container DNS names
- To switch profile:
  - Local run: `-Dspring.profiles.active=dev`
  - Docker/Compose: env `SPRING_PROFILES_ACTIVE=pro` (already set in compose)

## Service URLs used by Gateway
- dev profile
  - user-service.url = http://localhost:8081
  - auth-service.url = http://localhost:8082
  - customer-service.url = http://localhost:8083
  - property-service.url = http://localhost:8084
- pro profile
  - user-service.url = http://user-service:8081
  - auth-service.url = http://auth-service:8082
  - customer-service.url = http://customerservice:8083
  - property-service.url = http://propertyservice:8084

## Routes via Gateway (prefix `/api`)
- **Public**
  - POST `/api/auth/register`
  - POST `/api/auth/login`
  - POST `/api/auth/refresh`
- **Users** (requires JWT; role USER or ADMIN)
  - GET/POST `/api/users`
  - GET `/api/users/{id}`
  - GET `/api/users/username/{username}`
  - GET `/api/users/email/{email}`
  - PUT `/api/users/{id}`
  - DELETE `/api/users/{id}`
  - GET `/api/users/search?firstName=...`
  - GET `/api/users/status/{status}`
  - GET `/api/users/count`
- **Customers** (requires JWT; role MANAGER or ADMIN)
  - CRUD under `/api/customers/**`
- **Property** (requires JWT; role ADMIN) 
  - Endpoints under `/api/properties/**` (see codebase controllers)
- **Monitoring**
  - `/actuator/**`

## Roles and Permissions
- Gateway adds/validates headers via filters:
  - AuthenticationFilter validates JWT and adds `X-User-*` headers
  - RoleCheckFilter checks `X-Required-Role` per route
- Required roles by route group:
  - Users: `USER` (or `ADMIN`)
  - Customers: `MANAGER` (or `ADMIN`)
  - Properties: `ADMIN`

## Environment and Secrets
- JWT secret can be set via `JWT_SECRET` env in production.
- Database URLs are currently configured in each service `application.properties`. They point to a remote MySQL; update to use env vars if needed.

## Notes
- Elasticsearch is provided for development as a single-node container on 9200.
- Rate limiting requires Redis; filters are wired but disabled by default.

## Local Dev (without Docker)
- Start each service: `mvn spring-boot:run`
- Ensure ports: gateway 8080, user 8081, auth 8082, customers 8083, property 8084
- Use the `application-dev.yml` in gateway.
