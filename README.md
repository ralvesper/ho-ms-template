# ho-ms-template

Template multi-module Gradle para microsserviços Spring Boot 4 (Java 25). Pacote base: `com.highonline`.

## Estrutura

| Módulo | O que é |
|---|---|
| `microservices/common` | Código compartilhado: exceptions de domínio, `ApiExceptionHandler` (ProblemDetail), `PageModel` |
| `microservices/example-api` | API REST + JPA + Flyway + Postgres. Exemplo: `/api/v1/greetings` (ids TSID, trace UUIDv7, e-mail validado com commons-validator, cliente RestClient de tradução, contract tests) |
| `apps/example-web` | Web Thymeleaf mínimo que consome a API (porta 9080) |

## Estrutura de pacotes (hexagonal)

```
core/
  domain/model/<aggregate>/   entidades, value objects, interface do repositório (ex.: Greetings)
  domain/model/commons/       value objects compartilhados (Email...)
  ports/in/<aggregate>/       casos de uso (ForManagingX, ForQueryingX) + Input/Output
  ports/out/<aggregate>/      dependências externas (ForObtainingX, ForTranslatingText)
  application/<aggregate>/    application services (implementam ports/in)
infrastructure/
  adapters/in/web/<aggregate>/            controllers
  adapters/out/persistence/<aggregate>/   entity JPA, assembler/disassembler, provider
  adapters/out/web/<servico>/http/        clientes REST (implementam ports/out)
  config/                                 configurações transversais
```

`microservices/common` segue o mesmo padrão: `core.domain.model` (DomainException...) e
`infrastructure.adapters.in.web` (PageModel, `exceptionhandler`).

## Pré-requisitos

JDK 25 (`asdf install`, versão em `.tool-versions`) e Docker (Postgres local e testes `*IT`). O Gradle vem pelo wrapper.

## Rodando

```bash
docker compose up -d                          # Postgres (bancos exampleapi e exampleapi_test)
./gradlew :microservices:example-api:bootRun  # http://localhost:8080/api/v1/greetings
./gradlew :apps:example-web:bootRun           # http://localhost:9080
```

## Testes

```bash
./gradlew test              # unitários, sem Docker
./gradlew contractTest      # contratos (Spring Cloud Contract), sem Docker
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
3. Renomeie o pacote `com.highonline.exampleapi` e o `ExampleApiApplication`; use `greeting` como modelo de cada camada.
4. Troque `example-api` por `<nome>` no `build.gradle` (jar e imagem), no `Dockerfile` e no `spring.application.name`.
5. Troque o banco `exampleapi` nos `application-*-env.yml` e em `etc/postgres/init-user-db.sh`.
6. Apague os pacotes `greeting` de cada camada, a migration `V1__create_greeting.sql` e o contrato em `src/contractTest/resources/contracts` (ajuste o `ContractBase`).

## Convenções

- Versões de plugins, Java, Lombok e JUnit ficam no `build.gradle` da raiz; os módulos só declaram o que é específico.
- Falhas de serviços externos viram `BadGatewayException` (5xx), `GatewayTimeoutException` (I/O) ou `UnprocessableEntityException` (4xx), tratadas pelo `ApiExceptionHandler`.
- Testes que precisam de Docker terminam em `IT`.
- Migrations em `db/migration`; dados de exemplo em `db/testdata` (só no perfil `development`).
- Perfis: `development` (padrão), `docker`, `production`, todos incluindo `base`.
