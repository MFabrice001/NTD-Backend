package com.portfolio.backend.repositories;

import com.portfolio.backend.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
