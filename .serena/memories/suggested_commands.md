# Suggested Commands

## Build & Run
```bash
# Build (skip tests)
./mvnw -B -DskipTests clean package

# Build with tests
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run the JAR directly
java -jar target/pji-0.0.1-SNAPSHOT.jar
```

## Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=PatientServiceImplTest

# Run specific test method
./mvnw test -Dtest=PatientServiceImplTest#create_ValidData_ReturnsCreatedEntity
```

## Docker
```bash
# Build Docker image
docker build -t pji-backend .

# Run Docker container
docker run -p 8085:8085 pji-backend
```

## Utilities (Windows with Git Bash)
```bash
git status
git log --oneline -20
git diff
ls -la
find . -name "*.java" | head -20
grep -r "pattern" src/
```

## Database Migrations (Flyway)
- Prod migrations: `src/main/resources/db/migration/`
- Dev migrations: `src/main/resources/dev/db/migration/`
- Naming: `V{version}__{description}.sql` (e.g. `V1__create_users_table.sql`)
- Never modify existing migrations, always add new ones

## API Documentation
- Swagger UI: `http://localhost:8085/swagger-ui/index.html`
- API docs: `http://localhost:8085/v3/api-docs`
