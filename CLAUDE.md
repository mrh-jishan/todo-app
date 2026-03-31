# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./mvnw spring-boot:run        # Start the application
./mvnw test                   # Run all tests
./mvnw test -Dtest=ClassName  # Run a single test class
./mvnw clean package          # Build JAR (skips tests with -DskipTests)
./mvnw clean compile          # Compile only
```

## Architecture

Spring Boot 4.0.5 MVC application with Thymeleaf server-side rendering. Package root: `com.todo.app`.

**Stack:**
- **Web**: Spring MVC controllers → Thymeleaf templates (in `src/main/resources/templates/`)
- **Security**: Spring Security + OAuth2 Client (present as dependencies, not yet configured)
- **Data**: Spring Data JPA → PostgreSQL (production) / H2 (dev/test)
- **Migrations**: Liquibase (`src/main/resources/db/changelog/`)
- **API Docs**: SpringDoc OpenAPI (Swagger UI auto-generated at `/swagger-ui.html`)
- **Monitoring**: Spring Boot Actuator (`/actuator/health`, etc.)

**Layered structure to follow:**
```
Controllers → Services → JPA Repositories → Database
                    ↓
            Thymeleaf Templates (views)
```

**Database config:** `application.properties` currently only sets `spring.application.name=app`. H2 is on the classpath for dev/test; PostgreSQL driver is runtime-scoped for production. Add datasource properties as needed.

**Lombok** is on the classpath — use `@Data`, `@Builder`, `@RequiredArgsConstructor`, etc. to reduce boilerplate on entity and DTO classes.
