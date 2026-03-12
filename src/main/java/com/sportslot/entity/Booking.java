package com.sportslot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"bookings", "reviews", "password", "hibernateLazyInitializer"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @JsonIgnoreProperties({"slots", "reviews", "hibernateLazyInitializer"})
    private Venue venue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnoreProperties({"venue", "hibernateLazyInitializer"})
    private Slot slot;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    public enum BookingStatus { CONFIRMED, CANCELLED }

    @PrePersist
    public void prePersist() {
        this.bookedAt = LocalDateTime.now();
        if (this.status == null) this.status = BookingStatus.CONFIRMED;
    }
}
