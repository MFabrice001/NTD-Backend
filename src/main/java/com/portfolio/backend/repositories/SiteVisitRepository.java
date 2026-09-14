package com.portfolio.backend.repositories;

import com.portfolio.backend.models.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {
}
