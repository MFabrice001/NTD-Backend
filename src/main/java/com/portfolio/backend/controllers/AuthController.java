package com.portfolio.backend.controllers;

import com.portfolio.backend.models.User;
import com.portfolio.backend.payload.AuthRequests.*;
import com.portfolio.backend.payload.JwtResponse;
import com.portfolio.backend.repositories.UserRepository;
import com.portfolio.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JavaMailSender mailSender;

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody OtpRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByUsername(request.getEmail());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            // Send Email to realfabrice20@gmail.com for admin
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ntdbuilddesignsolution@gmail.com");
            message.setTo("realfabrice20@gmail.com"); // hardcoded as requested by user
            message.setSubject("Admin Login OTP - NTD Build");
            message.setText("Your OTP for Admin Login is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
            
            // Send asynchronously so the HTTP response returns immediately without blocking
            CompletableFuture.runAsync(() -> {
                try {
                    mailSender.send(message);
                } catch (Exception e) {
                    System.err.println("Failed to send OTP email: " + e.getMessage());
                }
            });

            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully to admin email.");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("User not found.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByUsername(request.getEmail());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() != null && user.getOtp().equals(request.getOtp()) && 
                user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                
                if (user.getIsFirstLogin() != null && user.getIsFirstLogin()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("requiresPasswordReset", true);
                    return ResponseEntity.ok(response);
                }

                // If not first login, authenticate using existing flow (mock password since OTP verified)
                // We'll generate a token manually since OTP is verified
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);

                return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), "ROLE_ADMIN"));
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired OTP.");
            }
        }
        return ResponseEntity.badRequest().body("User not found.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByUsername(request.getEmail());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() != null && user.getOtp().equals(request.getOtp()) && 
                user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                
                user.setPassword(encoder.encode(request.getNewPassword()));
                user.setIsFirstLogin(false);
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), "ROLE_ADMIN"));
            }
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
        return ResponseEntity.badRequest().body("User not found.");
    }
}
