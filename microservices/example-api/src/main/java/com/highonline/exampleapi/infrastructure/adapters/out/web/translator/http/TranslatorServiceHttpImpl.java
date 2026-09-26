package com.highonline.exampleapi.infrastructure.adapters.out.web.translator.http;

import com.highonline.common.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.highonline.common.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import com.highonline.common.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import com.highonline.exampleapi.core.ports.out.greeting.ForTranslatingText;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/** Cliente HTTP do serviço de tradução; traduz falhas HTTP para as exceptions do módulo common. */
@Component
public class TranslatorServiceHttpImpl implements ForTranslatingText {

    private record Request(String text, String lang) {}
    private record Response(String text) {}

    private final RestClient restClient;

    public TranslatorServiceHttpImpl(RestClient translatorRestClient) {
        this.restClient = translatorRestClient;
    }

    @Override
    public String translate(String text, String lang) {
        try {
            Response response = restClient.post().uri("/translate")
                    .body(new Request(text, lang))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new UnprocessableEntityException("Translator rejected the request: " + res.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new BadGatewayException("Translator failed: " + res.getStatusCode(), null);
                    })
                    .body(Response.class);
            return response.text();
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Translator unreachable or timed out", e);
        }
    }
}
