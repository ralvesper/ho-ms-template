# ho-ms-template

Gradle multi-module, Spring Boot 4, Java 25, pacote base `com.highonline`. Veja o README para estrutura e comandos.

- Versões/plugins comuns: `build.gradle` da raiz. Novo módulo: `include` no `settings.gradle`.
- Código compartilhado entre serviços vai em `microservices/common` (pacote `com.highonline.common`); os serviços usam `@SpringBootApplication(scanBasePackages = "com.highonline")`.
- Testes `*IT` = integração com Testcontainers (task `integrationTest`); `test` roda o resto.
- Sem git remoto; commits pequenos.
