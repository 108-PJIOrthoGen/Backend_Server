# Project Overview

**Name**: PJI (108POG Backend Server)
**Purpose**: Clinical Decision Support System — a medical backend for managing patients, episodes, clinical records, lab results, and AI-powered diagnosis recommendations.

## Tech Stack
- **Framework**: Spring Boot 3.5.11
- **Language**: Java 17
- **Database**: PostgreSQL (port 5433, database `pji_dev`)
- **Cache**: Redis (Jedis)
- **ORM**: Spring Data JPA + Hibernate
- **Security**: Spring Security + OAuth2 + JWT
- **Messaging**: RabbitMQ (async AI recommendation processing)
- **Storage**: MinIO (medical file/image storage)
- **Mapping**: MapStruct 1.6.3
- **Validation**: Jakarta Validation
- **API Docs**: SpringDoc OpenAPI (Swagger)
- **Migrations**: Flyway
- **Observability**: Micrometer + Prometheus + OpenTelemetry (OTLP)
- **Build**: Maven (mvnw wrapper)
- **Container**: Docker (eclipse-temurin:17)

## Base Package
`com.vietnam.pji`

## Server
- Port: 8085
- API prefix: `/api/v1`
- Swagger UI available at `/swagger-ui/`

## External Services
- AI Service: `http://localhost:8000` (diagnosis recommendations, RAG chat)
- MinIO: `http://localhost:9000`
- RabbitMQ: `localhost:5672`
- Redis: `localhost:6379`
