package com.highonline.exampleapi.greeting;

import com.highonline.common.BadGatewayException;
import com.highonline.common.GatewayTimeoutException;
import com.highonline.common.UnprocessableEntityException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/** Cliente de um serviço externo de tradução; traduz falhas HTTP para as exceptions do módulo common. */
@Component
public class TranslatorClient {

    private record Request(String text, String lang) {}
    private record Response(String text) {}

    private final RestClient restClient;

    public TranslatorClient(RestClient translatorRestClient) {
        this.restClient = translatorRestClient;
    }

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
