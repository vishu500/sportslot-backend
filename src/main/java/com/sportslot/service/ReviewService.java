package com.sportslot.service;

import com.sportslot.dto.request.ReviewRequest;
import com.sportslot.entity.*;
import com.sportslot.exception.ResourceNotFoundException;
import com.sportslot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public Review addReview(String userEmail, ReviewRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

        Review review = Review.builder()
                .user(user).venue(venue)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        reviewRepository.save(review);

        Double avg = reviewRepository.findAvgRatingByVenueId(venue.getId());
        long count = reviewRepository.countByVenueId(venue.getId());
        venue.setAvgRating(avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0);
        venue.setTotalReviews((int) count);
        venueRepository.save(venue);

        return review;
    }

    public List<Review> getVenueReviews(Long venueId) {
        venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        return reviewRepository.findByVenueId(venueId);
    }
}
