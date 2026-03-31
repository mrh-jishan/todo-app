# Todo App

A full-stack todo application built with Spring Boot, Thymeleaf, Spring Security, and PostgreSQL/H2.

## Features

- **Authentication** — register and login with BCrypt-hashed passwords
- **Todos** — create, edit, delete, and toggle completion
- **Comments** — any authenticated user can comment on any todo; authors can delete their own comments
- **Profiles** — H2 in-memory for development, PostgreSQL for production
- **Migrations** — schema managed exclusively by Liquibase (no DDL auto)
- **API docs** — Swagger UI auto-generated at `/swagger-ui.html`

## Quick start (dev)

Requires Java 21.

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080` using an in-memory H2 database. No configuration needed.

| URL | Description |
|-----|-------------|
| `http://localhost:8080/auth/register` | Create an account |
| `http://localhost:8080/todos` | Your todo list (requires login) |
| `http://localhost:8080/h2-console` | H2 database browser (dev only) |
| `http://localhost:8080/swagger-ui.html` | API documentation |
| `http://localhost:8080/actuator/health` | Health check |

H2 console connection: `jdbc:h2:mem:tododb`, user `sa`, no password.

## Production

Set the following environment variables and activate the `prod` profile:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/tododb
export DB_USERNAME=your_user
export DB_PASSWORD=your_password

SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw clean package -DskipTests
SPRING_PROFILES_ACTIVE=prod \
  DB_URL=jdbc:postgresql://... \
  DB_USERNAME=... \
  DB_PASSWORD=... \
  java -jar target/todo-app-0.0.1-SNAPSHOT.jar
```

## Commands

```bash
./mvnw spring-boot:run              # Run (dev profile)
./mvnw test                         # Run all tests
./mvnw test -Dtest=ClassName        # Run a single test class
./mvnw clean package                # Build JAR
./mvnw clean package -DskipTests    # Build JAR without running tests
```

## Architecture

```
HTTP Request
    ↓
Spring Security (form login / session)
    ↓
Controller  (AuthController, TodoController, CommentController)
    ↓
Service interface  (UserService, TodoService, CommentService)
    ↓
Service impl  (impl/)
    ↓
JPA Repository  →  Database (H2 / PostgreSQL)
    ↓
Mapper  →  DTO  →  Thymeleaf template  →  HTML response
```

**Package layout**

```
com.todo.app/
├── config/         SecurityConfig
├── controller/     AuthController, TodoController, CommentController
├── dto/            RegisterRequest, TodoRequest, TodoResponse
│                   CommentRequest, CommentResponse
├── entity/         User, Todo, Comment
├── mapper/         TodoMapper, CommentMapper
├── repository/     UserRepository, TodoRepository, CommentRepository
└── service/
    ├── UserService, TodoService, CommentService   (interfaces)
    └── impl/  UserServiceImpl, TodoServiceImpl, CommentServiceImpl
               UserDetailsServiceImpl
```

**Entity relationships**

```
User (1) ──── (N) Todo
User (1) ──── (N) Comment
Todo (1) ──── (N) Comment
```

Both `Todo` and `Comment` carry a `user_id` FK. Deleting a `User` cascades to their `Todo`s and `Comment`s. Deleting a `Todo` cascades to its `Comment`s.

**Database migrations** live in `src/main/resources/db/changelog/changes/`:

| File | Description |
|------|-------------|
| `001-create-users-table.sql` | `users` — username, email, password |
| `002-create-todos-table.sql` | `todos` — title, description, completed, FK → users |
| `003-create-comments-table.sql` | `comments` — content, FK → todos, FK → users |

**Security rules**

| Path | Access |
|------|--------|
| `/auth/**` | Public |
| `/css/**`, `/js/**` | Public |
| `/h2-console/**` | Public (CSRF disabled for this path) |
| `/actuator/health` | Public |
| Everything else | Authenticated |

**Profiles**

| Profile | Database | H2 Console | SQL logging |
|---------|----------|------------|-------------|
| `dev` (default) | H2 in-memory | Enabled at `/h2-console` | On |
| `prod` | PostgreSQL via env vars | Disabled | Off |
