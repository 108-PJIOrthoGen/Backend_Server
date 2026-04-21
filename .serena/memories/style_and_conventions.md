# Code Style & Conventions

## Architecture
- **Layered**: Controller → Service (Interface + Impl) → Repository
- **DTO separation**: Request DTOs for input, Response DTOs for output. Never expose entities directly.
- All APIs return `ResponseData<T>` wrapper: `{ status, message, data }`
- Pagination via `PaginationResultDTO` with `Meta { page, pageSize, pages, total }`

## Naming
- **Entities**: PascalCase (`Patient.java`)
- **Repositories**: `{Entity}Repository`
- **Service interfaces**: `{Entity}Service`
- **Service impls**: `{Entity}ServiceImpl`
- **Controllers**: `{Entity}Controller`
- **Request DTOs**: `{Entity}RequestDTO`
- **Mappers**: `{Entity}Mapper`
- **Exceptions**: `{Name}Exception`
- **Configs**: `{Feature}Configuration`
- **DB columns**: snake_case; Java fields: camelCase

## Key Patterns
- Use Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor`
- Constructor injection via `@RequiredArgsConstructor` (no `@Autowired` on fields)
- MapStruct for DTO ↔ Entity mapping (extends `EntityMapper` base interface)
- Spring Filter (`@Filter Specification<T>`) for dynamic queries
- `@Transactional` on service methods; `@Transactional(readOnly = true)` for reads
- `@Valid` for `@RequestBody` validation
- `@Validated` at controller class level
- `@Tag` for Swagger grouping
- `@ResponseStatus(HttpStatus.CREATED)` for POST endpoints
- Use `${api.prefix}` in `@RequestMapping`

## Exceptions
- `ResourceNotFoundException` → 404
- `InvalidDataException` → 409
- `BusinessException` → 400
- `ForbiddenException` → 403

## Must NOT
- Expose JPA entities directly in APIs
- Use field injection (`@Autowired`)
- Catch generic `Exception`
- Return null from service methods (throw exception)
- Mix business logic in controllers
- Use raw SQL without parameterization
- Hardcode secrets
