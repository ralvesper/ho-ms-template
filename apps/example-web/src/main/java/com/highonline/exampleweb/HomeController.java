package com.highonline.exampleweb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final RestClient api;

    public HomeController(RestClient.Builder builder, @Value("${example-api.url}") String apiUrl) {
        this.api = builder.baseUrl(apiUrl).build();
    }

    @GetMapping("/")
    @SuppressWarnings("unchecked")
    public String home(Model model) {
        try {
            Map<String, Object> page = api.get().uri("/api/v1/greetings").retrieve().body(Map.class);
            model.addAttribute("greetings", page == null ? List.of() : (List<Map<String, Object>>) page.get("content"));
        } catch (RestClientException e) {
            model.addAttribute("greetings", List.of());
            model.addAttribute("error", "API indisponível");
        }
        return "index";
    }
}
