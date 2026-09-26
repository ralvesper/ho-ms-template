package com.highonline.exampleapi;

import com.highonline.exampleapi.core.ports.in.greeting.ForManagingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.ForQueryingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.ForTranslatingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingOutput;
import com.highonline.exampleapi.infrastructure.adapters.in.web.greeting.GreetingController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Base dos testes gerados a partir de src/contractTest/resources/contracts; só a camada web, portas mockadas. */
@WebMvcTest(GreetingController.class)
public abstract class ContractBase {

    @Autowired
    WebApplicationContext context;

    @MockitoBean
    ForQueryingGreetings forQueryingGreetings;
    @MockitoBean
    ForManagingGreetings forManagingGreetings;
    @MockitoBean
    ForTranslatingGreetings forTranslatingGreetings;

    @BeforeEach
    void setUp() {
        Mockito.when(forQueryingGreetings.findById(Mockito.anyString())).thenReturn(
                new GreetingOutput("0DZ1XNY3F9Q3E", UUID.randomUUID(), "Hello, HighOnline!",
                        "hello@highonline.com", OffsetDateTime.now()));
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
