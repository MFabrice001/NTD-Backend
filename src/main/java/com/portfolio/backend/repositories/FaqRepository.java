package com.portfolio.backend.repositories;

import com.portfolio.backend.models.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
