package com.myproject.AgritradeHub.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String geminiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestTemplate rest = new RestTemplate();

    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) return "Prompt cannot be empty.";

        // ✅ Correct endpoint
        String endpoint = String.format("%s/%s:generateContent?key=%s", geminiUrl, model, geminiKey);

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = rest.exchange(endpoint, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> resp = response.getBody();
                if (resp.containsKey("candidates")) {
                    List candidates = (List) resp.get("candidates");
                    if (!candidates.isEmpty()) {
                        Map first = (Map) candidates.get(0);
                        Map contentMap = (Map) first.get("content");
                        if (contentMap != null && contentMap.containsKey("parts")) {
                            List parts = (List) contentMap.get("parts");
                            if (!parts.isEmpty()) {
                                Map firstPart = (Map) parts.get(0);
                                return firstPart.get("text").toString();
                            }
                        }
                    }
                }
            }
            return "No response text found.";
        } catch (Exception e) {
            return "Error calling Gemini API: " + e.getMessage();
        }
    }
}
