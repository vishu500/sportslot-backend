package com.sportslot.controller;

import com.sportslot.dto.request.ReviewRequest;
import com.sportslot.entity.Review;
import com.sportslot.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> add(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.addReview(userDetails.getUsername(), request));
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Review>> getByVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(reviewService.getVenueReviews(venueId));
    }
}
