package com.portfolio.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category; // e.g., "COMMERCIAL", "RESIDENTIAL", "CIVIL WORKS"

    private String location;
    
    private String year;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_url", columnDefinition = "TEXT")
    private List<String> imageUrls = new ArrayList<>();

    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        syncImages();
    }

    @PreUpdate
    protected void onUpdate() {
        syncImages();
    }

    private void syncImages() {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            if (imageUrl == null || imageUrl.isBlank()) {
                this.imageUrl = imageUrls.get(0);
            }
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrls = new ArrayList<>();
            this.imageUrls.add(imageUrl);
        }
    }

    public List<String> getImageUrls() {
        if ((imageUrls == null || imageUrls.isEmpty()) && imageUrl != null && !imageUrl.isBlank()) {
            List<String> list = new ArrayList<>();
            list.add(imageUrl);
            return list;
        }
        return imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
        if (!this.imageUrls.isEmpty() && (this.imageUrl == null || this.imageUrl.isBlank())) {
            this.imageUrl = this.imageUrls.get(0);
        }
    }
}
