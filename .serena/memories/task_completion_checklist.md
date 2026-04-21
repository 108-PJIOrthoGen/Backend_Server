# Task Completion Checklist

After completing a coding task, verify:

1. **Compilation**: Code compiles without errors (`./mvnw compile`)
2. **Tests**: All tests pass (`./mvnw test`)
3. **Validation**: Request DTOs have proper validation annotations (`@NotBlank`, `@NotNull`, etc.)
4. **Transactions**: Service methods annotated with `@Transactional`; read-only methods with `@Transactional(readOnly = true)`
5. **Exceptions**: Appropriate exception types used (ResourceNotFoundException, InvalidDataException, etc.)
6. **No hardcoded values**: Use constants or config properties
7. **Swagger**: `@Tag` annotation on controllers for API documentation grouping
8. **Response format**: Controllers return `ResponseData<T>`
9. **Security**: No secrets hardcoded, no credentials logged
10. **Migration**: If schema changed, add a new Flyway migration file (never modify existing ones)
