package com.sportslot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sport;

    @Column(nullable = false)
    private String location;

    private String address;
    private String description;

    @Column(name = "price_per_hour", nullable = false)
    private Double pricePerHour;

    private String amenities;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Venue owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"venues", "bookings", "reviews", "password", "hibernateLazyInitializer"})
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Slot> slots;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.avgRating == null) this.avgRating = 0.0;
        if (this.totalReviews == null) this.totalReviews = 0;
    }
}
