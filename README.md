# Task Management API

## Overview

**Task Management API** is a RESTful service inspired by Trello, designed to manage boards, lists, and cards.  

Developed in **Java 25 (LTS)** with **Spring Boot 4**, the project emphasises best practices in architecture,
security, testing, and observability.

---

## Features

- Secure user registration and authentication with JWT
- Role-based access control for API endpoints
- Boards creation, listing, and sharing
- Lists management within boards
- Cards CRUD, movement between lists, and user assignment
- API documentation via Swagger
- Observability with application metrics and health checks
- CI/CD with quality gates and automated tests

---

## Tech Stack

| Layer                             | Technology                                  |
|-----------------------------------|---------------------------------------------|
| **Language**                      | Java 25 (LTS)                               |
| **Framework**                     | Spring Boot 4                               |
| **Build & Dependency Management** | Gradle (Kotlin DSL)                         |
| **Security**                      | Spring Security + JWT                       |
| **Persistence / Database**        | Spring Data JPA, PostgreSQL, Flyway         |
| **API Documentation**             | Swagger / OpenAPI                           |
| **Testing**                       | JUnit 5, Mockito, Spring Test               |
| **CI/CD**                         | GitHub Actions, quality gates, code scans   |
| **Observability**                 | Spring Actuator, Prometheus metrics support |
| **Containerisation**              | Docker & Docker Compose                     |

---

## Getting Started

### Prerequisites

- **Optional:** Java 25 (LTS) – only needed if running the application locally
- Docker & Docker Compose
- Git

### Running with Docker

Run the entire application stack (PostgreSQL + pgAdmin + API) with a single command:

```bash     
# UNIX / macOS
./run.sh

# Windows
./run
```

### Running locally with Java

If you prefer running the application with your local JDK:

```bash
# Run PostgreSQL and pgAdmin
docker compose up -d

# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun
```

### Swagger UI

Once the application is running, you can access the Swagger UI at http://localhost:8080/swagger-ui.html.

## Contributing

- Develop features in feature branches named after the issue, e.g., feature/user-registration.
- PRs should merge into main only after passing CI/CD quality gates.
- Follow **[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)** for commit messages.

## License

MIT License
