package com.hamids.geminiai.controller;

import com.hamids.geminiai.service.GeminiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping
    public String hello() {
        return "Hello from Gemini Controller!";
    }

    @PostMapping("/ask")
    public ResponseEntity<String> getGeminiResponse(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = geminiService.getResponse(question);
        return ResponseEntity.ok(answer);
    }
}
