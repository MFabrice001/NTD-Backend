package com.portfolio.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_visits")
@Data
@NoArgsConstructor
public class SiteVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime visitedAt;

    public SiteVisit(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }
}
