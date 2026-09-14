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
import com.portfolio.backend.repositories.SiteVisitRepository;
import com.portfolio.backend.repositories.UserRepository;
import com.portfolio.backend.models.User;
import com.portfolio.backend.payload.AuthRequests.ProfileUpdateRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.portfolio.backend.repositories.SubscriberRepository;
import com.portfolio.backend.models.Subscriber;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AdminController {

    private final BlogService blogService;
    private final FaqService faqService;
    private final ProjectService projectService;
    private final TeamMemberService teamMemberService;
    private final ContactMessageService contactMessageService;
    private final ServiceService serviceService;
    private final SiteVisitRepository siteVisitRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriberRepository subscriberRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public AdminController(BlogService blogService, FaqService faqService, ProjectService projectService, TeamMemberService teamMemberService, ContactMessageService contactMessageService, ServiceService serviceService, SiteVisitRepository siteVisitRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, SubscriberRepository subscriberRepository, JavaMailSender mailSender) {
        this.blogService = blogService;
        this.faqService = faqService;
        this.projectService = projectService;
        this.teamMemberService = teamMemberService;
        this.contactMessageService = contactMessageService;
        this.serviceService = serviceService;
        this.siteVisitRepository = siteVisitRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriberRepository = subscriberRepository;
        this.mailSender = mailSender;
    }

    // --- Blogs ---
    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        Blog createdBlog = blogService.createBlog(blog);
        List<Subscriber> subscribers = subscriberRepository.findAll();
        for (Subscriber sub : subscribers) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(sub.getEmail());
                message.setSubject("New Blog Posted: " + createdBlog.getTitle());
                message.setText("Hello,\n\nWe just posted a new blog: " + createdBlog.getTitle() + "\n\nCheck it out on our website!\n\nBest,\nNTD BUILD & DESIGN SOLUTIONS");
                mailSender.send(message);
            } catch (Exception e) {
                // Ignore email failure for individual subscriber
            }
        }
        return ResponseEntity.ok(createdBlog);
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
        Project createdProject = projectService.createProject(project);
        List<Subscriber> subscribers = subscriberRepository.findAll();
        for (Subscriber sub : subscribers) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(sub.getEmail());
                message.setSubject("New Project Uploaded: " + createdProject.getTitle());
                message.setText("Hello,\n\nWe just uploaded a new project: " + createdProject.getTitle() + "\n\nCheck it out on our website!\n\nBest,\nNTD BUILD & DESIGN SOLUTIONS");
                mailSender.send(message);
            } catch (Exception e) {
                // Ignore email failure for individual subscriber
            }
        }
        return ResponseEntity.ok(createdProject);
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

    // --- Contact Messages ---
    @GetMapping("/contact-messages")
    public ResponseEntity<List<ContactMessage>> getAllContactMessages() {
        return ResponseEntity.ok(contactMessageService.getAllMessages());
    }

    @DeleteMapping("/contact-messages/{id}")
    public ResponseEntity<Void> deleteContactMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // --- Services ---
    @PostMapping("/services")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceService.createService(service));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        return ResponseEntity.ok(serviceService.updateService(id, service));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    // --- Analytics ---
    @GetMapping("/analytics/visits/count")
    public ResponseEntity<Long> getVisitCount() {
        return ResponseEntity.ok(siteVisitRepository.count());
    }

    // --- Profile ---
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody ProfileUpdateRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Update email/fullName if provided
            if (request.getFullName() != null) user.setFullName(request.getFullName());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            
            // Update password if requested
            if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                    return ResponseEntity.badRequest().body("Incorrect current password.");
                }
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }
            
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
