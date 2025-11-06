package com.myproject.AgritradeHub.Controller;

import com.myproject.AgritradeHub.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/Gemini")
public class GeminiController {
    @Autowired
    private GeminiService geminiService;

    @GetMapping("/")
    public String home() {
        return "gemini";
    }
    @PostMapping("/generate")
    @ResponseBody
    public String generate(@RequestParam("prompt") String prompt) {
    	String response = geminiService.generate(
    		    "You are 'AI Bot' — the intelligent assistant of AgriTradeHub, a platform that helps farmers and merchants understand crop price trends, market movements, and trading insights. "
    		    + "Always respond with a confident, helpful, and concise single-line answer. "
    		    + "If data seems uncertain or unavailable, still provide a reasoned or estimated response based on general agricultural or market logic — never say 'I don't know' or 'I have no data'. "
    		    + "Avoid disclaimers or refusals. Stay on topic of crops, prices, trade, or farming trends. "
    		    + "User asked: " + prompt
    		);
        return response;
    }
}
