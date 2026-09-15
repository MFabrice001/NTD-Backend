package com.portfolio.backend.controllers;

import com.portfolio.backend.models.Subscriber;
import com.portfolio.backend.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/public")
public class SubscriberController {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        if (subscriberRepository.existsByEmail(email)) {
            return ResponseEntity.ok().body("Already subscribed.");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        subscriberRepository.save(subscriber);
        
        return ResponseEntity.ok().body("Subscribed successfully.");
    }
}
