package com.hamids.geminiai.controller;

import com.hamids.geminiai.service.GeminiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    public ResponseEntity<String> getGeminiResponse(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = geminiService.getResponse(question);
        return ResponseEntity.ok(answer);
    }
}
