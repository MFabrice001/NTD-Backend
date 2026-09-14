package com.portfolio.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;
    
    private String fullName;
    
    @Column(unique = true)
    private String email;
    
    private String otp;
    
    private java.time.LocalDateTime otpExpiry;
    
    private Boolean isFirstLogin = true;
}
