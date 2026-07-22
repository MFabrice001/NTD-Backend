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

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AdminController {

    private final BlogService blogService;
    private final FaqService faqService;
    private final ProjectService projectService;
    private final TeamMemberService teamMemberService;

    @Autowired
    public AdminController(BlogService blogService, FaqService faqService, ProjectService projectService, TeamMemberService teamMemberService) {
        this.blogService = blogService;
        this.faqService = faqService;
        this.projectService = projectService;
        this.teamMemberService = teamMemberService;
    }

    // --- Blogs ---
    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        return ResponseEntity.ok(blogService.createBlog(blog));
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        try {
            return ResponseEntity.ok(blogService.updateBlog(id, blog));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    // --- FAQs ---
    @PostMapping("/faqs")
    public ResponseEntity<Faq> createFaq(@RequestBody Faq faq) {
        return ResponseEntity.ok(faqService.createFaq(faq));
    }

    @PutMapping("/faqs/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable Long id, @RequestBody Faq faq) {
        try {
            return ResponseEntity.ok(faqService.updateFaq(id, faq));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }

    // --- Projects ---
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        try {
            return ResponseEntity.ok(projectService.updateProject(id, project));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // --- Team Members ---
    @PostMapping("/team-members")
    public ResponseEntity<TeamMember> createTeamMember(@RequestBody TeamMember teamMember) {
        return ResponseEntity.ok(teamMemberService.createTeamMember(teamMember));
    }

    @PutMapping("/team-members/{id}")
    public ResponseEntity<TeamMember> updateTeamMember(@PathVariable Long id, @RequestBody TeamMember teamMember) {
        try {
            return ResponseEntity.ok(teamMemberService.updateTeamMember(id, teamMember));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/team-members/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        teamMemberService.deleteTeamMember(id);
        return ResponseEntity.noContent().build();
    }
}
