package com.portfolio.backend.config;

import com.portfolio.backend.models.User;
import com.portfolio.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        migrateDatabaseSchema();

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Default password
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Default admin user created: admin / admin123");
        }
    }

    private void migrateDatabaseSchema() {
        if (jdbcTemplate == null) return;

        String[] alterStatements = {
            "ALTER TABLE projects ALTER COLUMN image_url TYPE TEXT",
            "ALTER TABLE project_images ALTER COLUMN image_url TYPE TEXT",
            "ALTER TABLE services ALTER COLUMN icon TYPE TEXT",
            "ALTER TABLE services ALTER COLUMN description TYPE TEXT",
            "ALTER TABLE team_members ALTER COLUMN image_url TYPE TEXT",
            "ALTER TABLE team_members ALTER COLUMN quote TYPE TEXT",
            "ALTER TABLE blogs ALTER COLUMN image_url TYPE TEXT",
            "ALTER TABLE blogs ALTER COLUMN content TYPE TEXT"
        };

        for (String sql : alterStatements) {
            try {
                jdbcTemplate.execute(sql);
                System.out.println("[DB-MIGRATION] Executed: " + sql);
            } catch (Exception e) {
                System.out.println("[DB-MIGRATION] Note: " + sql + " -> " + e.getMessage());
            }
        }
    }
}
