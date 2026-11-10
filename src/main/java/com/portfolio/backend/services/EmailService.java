package com.portfolio.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:onboarding@resend.dev}")
    private String fromEmail;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public CompletableFuture<Boolean> sendEmailAsync(String to, String subject, String body) {
        return CompletableFuture.supplyAsync(() -> sendEmail(to, subject, body));
    }

    public boolean sendEmail(String to, String subject, String body) {
        if (resendApiKey == null || resendApiKey.trim().isEmpty()) {
            System.err.println(">>> [EmailService] RESEND_API_KEY is not configured. Email to " + to + " was skipped.");
            return false;
        }

        try {
            String payload = String.format(
                "{\"from\":\"NTD Build <%s>\",\"to\":[\"%s\"],\"subject\":\"%s\",\"text\":\"%s\"}",
                escapeJson(fromEmail),
                escapeJson(to),
                escapeJson(subject),
                escapeJson(body)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + resendApiKey.trim())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println(">>> [EmailService] Email sent successfully to " + to + " via Resend API: " + response.body());
                return true;
            } else {
                System.err.println(">>> [EmailService] Resend API error (status " + response.statusCode() + "): " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.err.println(">>> [EmailService] Exception while sending email to " + to + ": " + e.getMessage());
            return false;
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}

