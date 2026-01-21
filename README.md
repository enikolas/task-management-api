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

## Project Setup

This milestone establishes the **foundation of the project**, providing the basic structure and infrastructure needed for subsequent development:

- Initial Spring Boot skeleton with Gradle build
- Docker & Docker Compose for local development
- CI/CD pipeline with quality gates for PRs
- Swagger documentation setup for future endpoints
- Application metrics and health checks via Spring Actuator

Subsequent milestones will build on this foundation to implement business functionality,
security features, and complete API endpoints for boards, lists, and cards.

---

## Roadmap & Milestones

Milestones are tracked using **GitHub Milestones** and Issues:

- **v0.0.1 – Project Setup (foundation)**  
  Initial project skeleton, CI/CD pipeline, Swagger, metrics, Docker.

- **v0.1.0 – Authentication & Users**  
  User registration, login, JWT authentication, and secured endpoints.

- **v0.2.0 – Boards**  
  Create and list boards, share boards between users.

- **v0.3.0 – Lists**  
  Manage lists within boards.

- **v0.4.0 – Cards**  
  CRUD operations for cards, move between lists, assign users.

All issues are tracked publicly and can be accessed [here](https://github.com/enikolas/task-management-api/issues).

---

## Getting Started

### Prerequisites

- Java 25 (LTS)
- Docker & Docker Compose
- Git

### Local Setup

```bash
# Clone the repository
git clone https://github.com/enikolas/task-management-api.git
cd task-management-api

# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun
```

Swagger UI will be available at http://localhost:8080/swagger-ui.html.

## Contributing

- Develop features in feature branches named after the issue, e.g., feature/user-registration.
- PRs should merge into main only after passing CI/CD quality gates.
- Follow **[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)** for commit messages:
    ```
    feat(user): implement registration endpoint
    fix(auth): correct JWT token validation
    docs(readme): update setup instructions
    ```

## License

MIT License
