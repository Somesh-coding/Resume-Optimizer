package com.resumepilot.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenRouterService {
    @Value("${openrouter.api.key}")
    private String apiKey;
    @Value("${openrouter.api.url}")
    private String apiUrl;
    @Value("${openrouter.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generate(String prompt) {
        try {
            if (apiKey == null || apiKey.isBlank() || apiKey.contains("PASTE_YOUR")) {
                return "OpenRouter key missing. Add openrouter.api.key in application.properties.";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.set("HTTP-Referer", "http://localhost:5173");
            headers.set("X-OpenRouter-Title", "ResumePilot AI");

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", List.of(message));
            body.put("temperature", 0.72);
            body.put("max_tokens", 1800);

            System.out.println("Sending prompt to OpenRouter model: " + model);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, new HttpEntity<>(body, headers), Map.class);
            Map responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("choices")) {
                return "AI response failed: " + responseBody;
            }
            List choices = (List) responseBody.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map msg = (Map) firstChoice.get("message");

            return msg.get("content").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "AI generation failed. Check OpenRouter key, model name, or internet connection. Error: " + e.getMessage();
        }
    }
}
