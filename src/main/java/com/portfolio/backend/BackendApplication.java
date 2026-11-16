package com.portfolio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.portfolio.backend.models.User;
import com.portfolio.backend.repositories.UserRepository;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Try to find existing admin by username
            java.util.Optional<User> existingByUsername = userRepository.findByUsername("admin");
            java.util.Optional<User> existingByEmail = userRepository.findByEmail("m.fabrice001@gmail.com");

            if (existingByUsername.isPresent()) {
                // Admin user already exists - update email to match Resend registered account
                User admin = existingByUsername.get();
                if (!"m.fabrice001@gmail.com".equals(admin.getEmail())) {
                    admin.setEmail("m.fabrice001@gmail.com");
                    userRepository.save(admin);
                    System.out.println("Updated admin email to: m.fabrice001@gmail.com");
                } else {
                    System.out.println("Admin user already configured correctly.");
                }
            } else if (existingByEmail.isEmpty()) {
                // No admin exists at all - create one
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("m.fabrice001@gmail.com");
                admin.setFullName("System Administrator");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setIsFirstLogin(true);
                userRepository.save(admin);
                System.out.println("Default admin user created: m.fabrice001@gmail.com");
            }
        };
    }
}
