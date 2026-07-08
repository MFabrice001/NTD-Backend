package com.portfolio.backend.controllers;

import com.portfolio.backend.models.Blog;
import com.portfolio.backend.models.Faq;
import com.portfolio.backend.services.BlogService;
import com.portfolio.backend.services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {

    private final BlogService blogService;
    private final FaqService faqService;

    @Autowired
    public PublicController(BlogService blogService, FaqService faqService) {
        this.blogService = blogService;
        this.faqService = faqService;
    }

    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<Faq>> getAllFaqs() {
        return ResponseEntity.ok(faqService.getAllFaqs());
    }
}
