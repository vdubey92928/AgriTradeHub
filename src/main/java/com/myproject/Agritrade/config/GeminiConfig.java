package com.myproject.Agritrade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:}")
    private String apiUrl;

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("❌ Gemini API Key NOT set. Set gemini.api.key in application.properties or as env var GEMINI_API_KEY.");
        } else {
            System.out.println("✅ Gemini API Key loaded (length: " + apiKey.length() + ").");
        }
        System.out.println("🔗 Gemini API URL: " + apiUrl);
    }
}
