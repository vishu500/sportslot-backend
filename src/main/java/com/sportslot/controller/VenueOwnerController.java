package com.sportslot.controller;

import com.sportslot.dto.request.SlotRequest;
import com.sportslot.dto.request.VenueRequest;
import com.sportslot.entity.Booking;
import com.sportslot.entity.Slot;
import com.sportslot.entity.Venue;
import com.sportslot.service.VenueOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('VENUE_OWNER') or hasRole('ADMIN')")
public class VenueOwnerController {

    private final VenueOwnerService venueOwnerService;

    // GET /api/owner/venues
    @GetMapping("/venues")
    public ResponseEntity<List<Venue>> getMyVenues(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            venueOwnerService.getMyVenues(userDetails.getUsername()));
    }

    // POST /api/owner/venues
    @PostMapping("/venues")
    public ResponseEntity<Venue> createVenue(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VenueRequest request) {
        return ResponseEntity.ok(
            venueOwnerService.createVenue(userDetails.getUsername(), request));
    }

    // PUT /api/owner/venues/{id}
    @PutMapping("/venues/{id}")
    public ResponseEntity<Venue> updateVenue(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VenueRequest request) {
        return ResponseEntity.ok(
            venueOwnerService.updateVenue(id, userDetails.getUsername(), request));
    }

    // DELETE /api/owner/venues/{id}
    @DeleteMapping("/venues/{id}")
    public ResponseEntity<Void> deleteVenue(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        venueOwnerService.deleteVenue(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // POST /api/owner/venues/{id}/slots
    @PostMapping("/venues/{id}/slots")
    public ResponseEntity<Slot> addSlot(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SlotRequest request) {
        return ResponseEntity.ok(
            venueOwnerService.addSlot(id, userDetails.getUsername(), request));
    }

    // DELETE /api/owner/venues/{venueId}/slots/{slotId}
    @DeleteMapping("/venues/{venueId}/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Long venueId,
            @PathVariable Long slotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        venueOwnerService.deleteSlot(venueId, slotId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // GET /api/owner/bookings
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            venueOwnerService.getMyVenueBookings(userDetails.getUsername()));
    }
}
