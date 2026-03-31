# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./mvnw spring-boot:run        # Start the application (dev profile active by default)
./mvnw test                   # Run all tests
./mvnw test -Dtest=ClassName  # Run a single test class
./mvnw clean package          # Build JAR (skips tests with -DskipTests)
./mvnw clean compile          # Compile only
```

Run with prod profile: `SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run`

## Architecture

Spring Boot 4.0.5 MVC application with Thymeleaf server-side rendering. Package root: `com.todo.app`.

**Stack:**
- **Web**: Spring MVC controllers → Thymeleaf templates (`src/main/resources/templates/`)
- **Security**: Spring Security form-login with `UserDetailsServiceImpl`; BCrypt password encoding
- **Data**: Spring Data JPA → PostgreSQL (prod) / H2 in-memory (dev)
- **Migrations**: Liquibase (`db/changelog/db.changelog-master.yaml` → `changes/*.sql`)
- **API Docs**: SpringDoc OpenAPI (Swagger UI at `/swagger-ui.html`)
- **Monitoring**: Spring Boot Actuator (`/actuator/health`)

**Layered structure:**
```
Controllers → Services (interface + impl/) → JPA Repositories → Database
                     ↓
             Thymeleaf Templates + DTOs + Mappers
```

**Package layout:**
```
com.todo.app/
├── config/          SecurityConfig.java
├── controller/      AuthController, TodoController
├── dto/             RegisterRequest, TodoRequest, TodoResponse
├── entity/          User, Todo  (JPA, @PrePersist/@PreUpdate lifecycle hooks)
├── mapper/          TodoMapper  (manual entity → DTO mapping)
├── repository/      UserRepository, TodoRepository
└── service/
    ├── UserService.java, TodoService.java  (interfaces)
    └── impl/        UserServiceImpl, TodoServiceImpl, UserDetailsServiceImpl
```

**Database config by profile:**
- `dev` — H2 in-memory (`jdbc:h2:mem:tododb`); H2 Console at `/h2-console`; `show-sql=true`
- `prod` — PostgreSQL via env vars `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

Both profiles use `ddl-auto=none` — schema is managed exclusively by Liquibase.

**Security rules** (`SecurityConfig`): `/auth/**`, `/css/**`, `/js/**`, `/h2-console/**`, `/actuator/health` are public; everything else requires authentication. CSRF is disabled only for `/h2-console/**`. H2 Console frames allowed via `sameOrigin` frame options.

**Entity relationships:** `User` (1) → (N) `Todo` via `user_id` FK with cascade delete. Both entities set timestamps via `@PrePersist`/`@PreUpdate`.

**Templates** use Bootstrap 5.3 (CDN) and a shared navbar fragment (`fragments/navbar.html`) with Thymeleaf Security extras (`sec:authorize`, `sec:authentication`).
