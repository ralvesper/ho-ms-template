# ho-ms-template

Template multi-module (Gradle) — pacote base `com.highonline`.

- `microservices/common` — exceptions, `ApiExceptionHandler`, `PageModel` compartilhados
- `microservices/example-api` — API REST + JPA + Flyway + Postgres (`/api/v1/greetings`)
- `apps/example-web` — web Thymeleaf mínimo que consome a API

```
docker compose up -d                       # postgres
./gradlew :microservices:example-api:bootRun
./gradlew :apps:example-web:bootRun        # http://localhost:9080
```

Novo microsserviço: pasta em `microservices/`, `include` no `settings.gradle`, `implementation project(':microservices:common')`.
