package com.highonline.exampleapi;

import com.highonline.exampleapi.greeting.Greeting;
import com.highonline.exampleapi.greeting.GreetingRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

/** Base dos testes gerados a partir de src/test/resources/contracts; sem banco: o repositório é mock. */
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration,"
                + "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
})
public abstract class ContractBase {

    @Autowired
    WebApplicationContext context;

    @MockitoBean
    GreetingRepository repository;

    @BeforeEach
    void setUp() {
        Greeting greeting = new Greeting("Hello, HighOnline!", "hello@highonline.com");
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(greeting));
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
