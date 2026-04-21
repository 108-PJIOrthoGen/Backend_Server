# Project Structure

```
src/main/java/com/vietnam/pji/
├── PjiApplication.java              # Main entry point
├── config/                          # Configuration classes
│   ├── auth/                        # Security (JWT, CORS, interceptors)
│   ├── integration/                 # External services (AI, MinIO, Redis, OpenAPI)
│   └── properties/                  # Custom config properties
├── constant/                        # Enums (GenderEnum, RunStatus, ChatType, etc.)
├── controller/
│   ├── auth/                        # Auth, User, Role, Permission controllers
│   ├── medical/                     # Patient, Episode, ClinicalRecord, LabResult, etc.
│   └── agentic/                     # AI chat controller
├── dto/
│   ├── request/                     # Input DTOs with validation
│   └── response/                    # Output DTOs (ResponseData, PaginationResultDTO)
├── exception/                       # GlobalExceptionHandler + custom exceptions
├── message/                         # RabbitMQ publisher
├── model/
│   ├── AbstractEntity.java          # Base entity (id, audit fields)
│   ├── auth/                        # User, Role, Permission
│   ├── medical/                     # Patient, Episode, ClinicalRecord, LabResult, etc.
│   └── agentic/                     # AiChatSession, AiRecommendationRun, etc.
├── repository/                      # JPA repositories
├── services/                        # Service interfaces
│   └── impl/                        # Service implementations
└── utils/
    ├── SecurityUtils.java           # Get current user
    ├── mapper/                      # MapStruct mappers
    └── validators/                  # Custom validators (EnumPattern)

src/main/resources/
├── application.yaml                 # Main config
├── application-dev.yml              # Dev profile
├── application-prod.yml             # Prod profile
├── db/migration/                    # Flyway migrations (prod)
└── dev/db/migration/                # Flyway migrations (dev)
```
