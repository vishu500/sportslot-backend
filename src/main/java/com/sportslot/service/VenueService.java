package com.sportslot.service;

import com.sportslot.entity.Slot;
import com.sportslot.entity.Venue;
import com.sportslot.exception.ResourceNotFoundException;
import com.sportslot.repository.SlotRepository;
import com.sportslot.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final SlotRepository slotRepository;

    public List<Venue> searchVenues(String query, String sport) {
        if (sport != null && !sport.isBlank() && query != null && !query.isBlank())
            return venueRepository.searchBySportAndQuery(sport, query);
        if (sport != null && !sport.isBlank())
            return venueRepository.findBySportIgnoreCase(sport);
        if (query != null && !query.isBlank())
            return venueRepository.searchVenues(query);
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found: " + id));
    }

    public List<Slot> getAllSlots(Long venueId, LocalDate date) {
        getVenueById(venueId);
        return slotRepository.findByVenueIdAndDate(venueId, date);
    }

    public List<Slot> getAvailableSlots(Long venueId, LocalDate date) {
        getVenueById(venueId);
        return slotRepository.findByVenueIdAndDateAndStatus(venueId, date, Slot.SlotStatus.AVAILABLE);
    }
}
