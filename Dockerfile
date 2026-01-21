FROM gradle:9.2.1-jdk25-ubi AS builder

WORKDIR /opt/task-management-api

COPY . .
RUN gradle bootJar -x test --no-daemon --parallel

FROM eclipse-temurin:25-jre-noble

ARG VERSION

LABEL maintainer="Everton Nikolas de Oliveira <https://github.com/enikolas>"
LABEL version=${VERSION:-latest}
LABEL description="Task Management API - A RESTful service inspired by Trello, designed to manage boards, lists, and cards."

RUN useradd -ms /bin/bash taskmanagementapi
USER taskmanagementapi

WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

COPY --from=builder /opt/task-management-api/build/libs/task-management-api-*.jar task-management-api.jar

ENTRYPOINT ["java", "-jar", "task-management-api.jar"]
