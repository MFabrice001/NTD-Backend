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
    private final JavaMailSender mailSender;

    @Autowired
    public ContactMessageService(ContactMessageRepository repository, JavaMailSender mailSender) {
        this.repository = repository;
        this.mailSender = mailSender;
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

        // Send email notification
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("ntdbuilddesignsolution@gmail.com");
            mailMessage.setFrom("ntdbuilddesignsolution@gmail.com");
            mailMessage.setSubject("New Contact Message from " + message.getFirstName() + " " + message.getLastName());
            mailMessage.setText(
                "You have received a new contact message on your website.\n\n" +
                "Name: " + message.getFirstName() + " " + message.getLastName() + "\n" +
                "Email: " + message.getEmail() + "\n" +
                "Phone: " + (message.getNumber() != null ? message.getNumber() : "N/A") + "\n\n" +
                "Message:\n" + message.getMessage() + "\n\n" +
                "Log in to the Admin Dashboard to manage your messages."
            );
            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.err.println("Failed to send email notification: " + e.getMessage());
        }

        return savedMessage;
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}
