package com.portfolio.backend.controllers;

import com.portfolio.backend.payload.JwtResponse;
import com.portfolio.backend.payload.LoginRequest;
import com.portfolio.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        org.springframework.security.core.userdetails.User userDetails = 
            (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, 1L, userDetails.getUsername(), "ROLE_ADMIN"));
    }
}
