package com.highonline.exampleapi.infrastructure.adapters.out.web.translator.http;

import com.highonline.common.infrastructure.adapters.out.web.BadGatewayException;
import com.highonline.common.infrastructure.adapters.out.web.GatewayTimeoutException;
import com.highonline.common.infrastructure.adapters.out.web.UnprocessableEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

class TranslatorServiceHttpImplTest {

    MockRestServiceServer server;
    TranslatorServiceHttpImpl client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://translator");
        server = MockRestServiceServer.bindTo(builder).build();
        client = new TranslatorServiceHttpImpl(builder.build());
    }


    @Test
    void returnsTranslatedText() {
        server.expect(requestTo("http://translator/translate"))
                .andRespond(withSuccess("{\"text\":\"hello\"}", MediaType.APPLICATION_JSON));
        assertThat(client.translate("oi", "en")).isEqualTo("hello");
    }

    @Test
    void maps5xxToBadGateway() {
        server.expect(requestTo("http://translator/translate")).andRespond(withStatus(INTERNAL_SERVER_ERROR));
        assertThatThrownBy(() -> client.translate("oi", "en")).isInstanceOf(BadGatewayException.class);
    }

    @Test
    void maps4xxToUnprocessable() {
        server.expect(requestTo("http://translator/translate")).andRespond(withStatus(UNPROCESSABLE_ENTITY));
        assertThatThrownBy(() -> client.translate("oi", "xx")).isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void mapsIoFailureToGatewayTimeout() {
        server.expect(requestTo("http://translator/translate")).andRespond(request -> {
            throw new SocketTimeoutException("read timed out");
        });
        assertThatThrownBy(() -> client.translate("oi", "en")).isInstanceOf(GatewayTimeoutException.class);
    }
}
