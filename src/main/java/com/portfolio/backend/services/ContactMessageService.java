package com.portfolio.backend.services;

import com.portfolio.backend.models.ContactMessage;
import com.portfolio.backend.repositories.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;
    private final EmailService emailService;

    @Autowired
    public ContactMessageService(ContactMessageRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<ContactMessage> getAllMessages() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<ContactMessage> getMessageById(Long id) {
        return repository.findById(id);
    }

    public ContactMessage createMessage(ContactMessage message) {
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(LocalDateTime.now());
        }
        ContactMessage savedMessage = repository.save(message);

        // Send email notification asynchronously via Resend API
        String emailBody = String.format(
            "You have received a new contact message on your website.\n\n" +
            "Name: %s %s\n" +
            "Email: %s\n" +
            "Phone: %s\n\n" +
            "Message:\n%s\n\n" +
            "Log in to the Admin Dashboard to manage your messages.",
            message.getFirstName(), message.getLastName(),
            message.getEmail(),
            (message.getNumber() != null ? message.getNumber() : "N/A"),
            message.getMessage()
        );
        emailService.sendEmailAsync("ntdbuilddesignsolution@gmail.com", "New Contact Message from " + message.getFirstName() + " " + message.getLastName(), emailBody);

        return savedMessage;
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}
