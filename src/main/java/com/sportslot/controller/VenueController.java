package com.sportslot.controller;

import com.sportslot.entity.Slot;
import com.sportslot.entity.Venue;
import com.sportslot.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<List<Venue>> getVenues(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String sport) {
        return ResponseEntity.ok(venueService.searchVenues(query, sport));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<List<Slot>> getSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(venueService.getAllSlots(id, date));
    }

    @GetMapping("/{id}/slots/available")
    public ResponseEntity<List<Slot>> getAvailableSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(venueService.getAvailableSlots(id, date));
    }
}
