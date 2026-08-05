package com.portfolio.backend.services;

import com.portfolio.backend.models.ContactMessage;
import com.portfolio.backend.repositories.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;

    @Autowired
    public ContactMessageService(ContactMessageRepository repository) {
        this.repository = repository;
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
        return repository.save(message);
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}
