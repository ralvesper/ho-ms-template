# ho-ms-template

Template multi-module Gradle para microsserviços Spring Boot 4 (Java 25). Pacote base: `com.highonline`.

## Estrutura

| Módulo | O que é |
|---|---|
| `template/` | Esqueleto de microsserviço, sem domínio: camadas vazias, config, Flyway, actuator, testes de arquitetura e de contexto. Ponto de partida de novos serviços |
| `microservices/common` | Código compartilhado: exceptions de domínio, `ApiExceptionHandler` (ProblemDetail), `PageModel` |
| `microservices/example-api` | O `template` com um exemplo mínimo preenchido: `/api/v1/greetings` (criar e buscar), uma camada de cada tipo |
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
  adapters/in/listener/<aggregate>/       listeners de eventos de domínio
  adapters/out/persistence/<aggregate>/   entity JPA, assembler/disassembler, provider
  adapters/out/web/<servico>/http/        clientes REST (implementam ports/out)
  config/                                 configurações transversais
```

`microservices/common` segue o mesmo padrão: `core.domain.model` (DomainException...) e
`infrastructure.adapters.in.web` (PageModel, `exceptionhandler`).

## Eventos de domínio

A entidade de domínio estende `AbstractEventSourceEntity` (`common`) e chama `publishDomainEvent(...)`
(ex.: `Greeting.brandNew` → `GreetingCreatedEvent`). O provider de persistência copia os eventos para a
entity JPA (`AbstractAggregateRoot`) e, no `save()`, o Spring Data os publica; um `@EventListener` em
`adapters/in/listener` reage a eles. Depois do `save` o provider limpa os eventos do agregado.

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

1. Copie `template/` para `microservices/<nome>` (sem a pasta `build`).
2. Adicione `include 'microservices:<nome>'` no `settings.gradle`.
3. Renomeie o pacote `com.highonline.template` e a classe `TemplateApplication`.
4. Troque `template` por `<nome>` no `build.gradle` (jar e imagem), no `Dockerfile`, no `spring.application.name` e nos `application-*-env.yml` (nome do banco).
5. Crie o banco em `etc/postgres/init-user-db.sh`.
6. Use `microservices/example-api` como modelo de cada camada (`greeting`).

O `template/` já traz as camadas vazias (inclusive `adapters/in/listener`); a base de eventos (`AbstractEventSourceEntity`) vem do `common`. Para usá-la, siga o padrão de "Eventos de domínio" acima, com `example-api` como modelo.

`template/` fica no build para não quebrar sem ninguém perceber; remova-o do `settings.gradle` em um projeto derivado se não precisar mais dele.

## Convenções

- Versões de plugins, Java, Lombok e JUnit ficam no `build.gradle` da raiz; os módulos só declaram o que é específico.
- Falhas de serviços externos viram `BadGatewayException` (5xx), `GatewayTimeoutException` (I/O) ou `UnprocessableEntityException` (4xx), tratadas pelo `ApiExceptionHandler`.
- Testes que precisam de Docker terminam em `IT`.
- Migrations em `db/migration`; dados de exemplo em `db/testdata` (só no perfil `development`).
- Perfis: `development` (padrão), `docker`, `production`, todos incluindo `base`.
