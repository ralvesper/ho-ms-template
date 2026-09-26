# ho-ms-template

Template multi-module Gradle para microsserviços Spring Boot 4 (Java 25). Pacote base: `com.highonline`.

## Estrutura

| Módulo | O que é |
|---|---|
| `microservices/common` | Código compartilhado: exceptions de domínio, `ApiExceptionHandler` (ProblemDetail), `PageModel` |
| `microservices/example-api` | API REST + JPA + Flyway + Postgres. Exemplo: `/api/v1/greetings` |
| `apps/example-web` | Web Thymeleaf mínimo que consome a API (porta 9080) |

## Pré-requisitos

JDK 25 e Docker (Postgres local e testes `*IT`). O Gradle vem pelo wrapper.

## Rodando

```bash
docker compose up -d                          # Postgres (bancos exampleapi e exampleapi_test)
./gradlew :microservices:example-api:bootRun  # http://localhost:8080/api/v1/greetings
./gradlew :apps:example-web:bootRun           # http://localhost:9080
```

## Testes

```bash
./gradlew test              # unitários, sem Docker
./gradlew integrationTest   # classes *IT, Testcontainers (precisa de Docker)
./gradlew build             # tudo
```

## Docker

```bash
./gradlew :microservices:example-api:dockerBuild   # highonline/example-api:dev
./gradlew :apps:example-web:dockerBuild            # highonline/example-web:dev
```

## Criando um novo microsserviço

1. Copie `microservices/example-api` para `microservices/<nome>`.
2. Adicione `include 'microservices:<nome>'` no `settings.gradle`.
3. Renomeie o pacote `com.highonline.exampleapi` e o `ExampleApiApplication`.
4. Troque `example-api` por `<nome>` no `build.gradle` (jar e imagem), no `Dockerfile` e no `spring.application.name`.
5. Troque o banco `exampleapi` nos `application-*-env.yml` e em `etc/postgres/init-user-db.sh`.
6. Apague o pacote `greeting` e a migration `V1__create_greeting.sql`.

## Convenções

- Versões de plugins, Java, Lombok e JUnit ficam no `build.gradle` da raiz; os módulos só declaram o que é específico.
- Testes que precisam de Docker terminam em `IT`.
- Migrations em `db/migration`; dados de exemplo em `db/testdata` (só no perfil `development`).
- Perfis: `development` (padrão), `docker`, `production`, todos incluindo `base`.
