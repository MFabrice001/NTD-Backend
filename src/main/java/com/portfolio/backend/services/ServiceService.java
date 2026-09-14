package com.portfolio.backend.services;

import com.portfolio.backend.models.Service;
import com.portfolio.backend.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository repository;

    @Autowired
    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<Service> getAllServices() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Service> getServiceById(Long id) {
        return repository.findById(id);
    }

    public Service createService(Service service) {
        if (service.getCreatedAt() == null) {
            service.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(service);
    }

    public Service updateService(Long id, Service serviceDetails) {
        return repository.findById(id).map(service -> {
            service.setTitle(serviceDetails.getTitle());
            service.setDescription(serviceDetails.getDescription());
            service.setIcon(serviceDetails.getIcon());
            return repository.save(service);
        }).orElseThrow(() -> new RuntimeException("Service not found with id " + id));
    }

    public void deleteService(Long id) {
        repository.deleteById(id);
    }
}
