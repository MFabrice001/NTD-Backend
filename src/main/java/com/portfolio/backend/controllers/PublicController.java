package com.portfolio.backend.controllers;

import com.portfolio.backend.models.Blog;
import com.portfolio.backend.models.ContactMessage;
import com.portfolio.backend.models.Faq;
import com.portfolio.backend.models.Project;
import com.portfolio.backend.models.TeamMember;
import com.portfolio.backend.services.BlogService;
import com.portfolio.backend.services.ContactMessageService;
import com.portfolio.backend.services.FaqService;
import com.portfolio.backend.services.ProjectService;
import com.portfolio.backend.services.TeamMemberService;
import com.portfolio.backend.services.ServiceService;
import com.portfolio.backend.models.Service;
import com.portfolio.backend.models.SiteVisit;
import com.portfolio.backend.repositories.SiteVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")

public class PublicController {

    private final BlogService blogService;
    private final FaqService faqService;
    private final ProjectService projectService;
    private final TeamMemberService teamMemberService;
    private final ContactMessageService contactMessageService;
    private final ServiceService serviceService;
    private final SiteVisitRepository siteVisitRepository;

    @Autowired
    public PublicController(BlogService blogService, FaqService faqService, ProjectService projectService, TeamMemberService teamMemberService, ContactMessageService contactMessageService, ServiceService serviceService, SiteVisitRepository siteVisitRepository) {
        this.blogService = blogService;
        this.faqService = faqService;
        this.projectService = projectService;
        this.teamMemberService = teamMemberService;
        this.contactMessageService = contactMessageService;
        this.serviceService = serviceService;
        this.siteVisitRepository = siteVisitRepository;
    }

    @PostMapping("/contact-messages")
    public ResponseEntity<ContactMessage> submitContactMessage(@RequestBody ContactMessage message) {
        return ResponseEntity.ok(contactMessageService.createMessage(message));
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

    @GetMapping("/services")
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @PostMapping("/visit")
    public ResponseEntity<Void> logVisit() {
        siteVisitRepository.save(new SiteVisit(java.time.LocalDateTime.now()));
        return ResponseEntity.ok().build();
    }
}
