package com.portfolio.backend.controllers;

import com.portfolio.backend.models.Blog;
import com.portfolio.backend.models.Faq;
import com.portfolio.backend.models.Project;
import com.portfolio.backend.models.TeamMember;
import com.portfolio.backend.services.BlogService;
import com.portfolio.backend.services.FaqService;
import com.portfolio.backend.services.ProjectService;
import com.portfolio.backend.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class PublicController {

    private final BlogService blogService;
    private final FaqService faqService;
    private final ProjectService projectService;
    private final TeamMemberService teamMemberService;

    @Autowired
    public PublicController(BlogService blogService, FaqService faqService, ProjectService projectService, TeamMemberService teamMemberService) {
        this.blogService = blogService;
        this.faqService = faqService;
        this.projectService = projectService;
        this.teamMemberService = teamMemberService;
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

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/team-members")
    public ResponseEntity<List<TeamMember>> getAllTeamMembers() {
        return ResponseEntity.ok(teamMemberService.getAllTeamMembers());
    }
}
