package com.hamids.geminiai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final Semaphore geminiLimiter = new Semaphore(1); // only 1 Gemini request at a time

    @Value("${google.api.url}")
    private String googleApiUrl;

    @Value("${google.api.key}")
    private String googleApiKey;

    public GeminiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getResponse(String question) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );

        try {
            geminiLimiter.acquire();

            return webClient.post()
                    .uri(googleApiUrl + googleApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(
                            Retry.backoff(5, Duration.ofSeconds(2))
                                    .filter(ex -> ex instanceof WebClientResponseException.TooManyRequests)
                    )
                    .onErrorReturn("{\"error\":\"Gemini is busy. Try again in a moment.\"}")
                    .block();

        } catch (InterruptedException e) {
            throw new RuntimeException("Gemini call interrupted", e);
        } finally {
            geminiLimiter.release();
        }
    }
}

