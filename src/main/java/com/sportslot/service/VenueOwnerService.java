package com.sportslot.service;

import com.sportslot.dto.request.SlotRequest;
import com.sportslot.dto.request.VenueRequest;
import com.sportslot.entity.*;
import com.sportslot.exception.ResourceNotFoundException;
import com.sportslot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueOwnerService {

    private final VenueRepository venueRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    // Get owner by email or throw
    private User getOwner(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Get venue owned by this owner or throw
    private Venue getOwnedVenue(Long venueId, String ownerEmail) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

        if (venue.getOwner() == null || !venue.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You don't own this venue!");
        }
        return venue;
    }

    // Create new venue
    public Venue createVenue(String ownerEmail, VenueRequest request) {
        User owner = getOwner(ownerEmail);

        Venue venue = Venue.builder()
                .name(request.getName())
                .sport(request.getSport())
                .location(request.getLocation())
                .address(request.getAddress())
                .description(request.getDescription())
                .pricePerHour(request.getPricePerHour())
                .amenities(request.getAmenities())
                .owner(owner)
                .build();

        return venueRepository.save(venue);
    }

    // Update existing venue
    public Venue updateVenue(Long venueId, String ownerEmail, VenueRequest request) {
        Venue venue = getOwnedVenue(venueId, ownerEmail);

        venue.setName(request.getName());
        venue.setSport(request.getSport());
        venue.setLocation(request.getLocation());
        venue.setAddress(request.getAddress());
        venue.setDescription(request.getDescription());
        venue.setPricePerHour(request.getPricePerHour());
        venue.setAmenities(request.getAmenities());

        return venueRepository.save(venue);
    }

    // Delete venue
    @Transactional
    public void deleteVenue(Long venueId, String ownerEmail) {
        Venue venue = getOwnedVenue(venueId, ownerEmail);
        venueRepository.delete(venue);
    }

    // Get all venues owned by this user
    public List<Venue> getMyVenues(String ownerEmail) {
        User owner = getOwner(ownerEmail);
        return venueRepository.findByOwnerId(owner.getId());
    }

    // Add a slot to a venue
    public Slot addSlot(Long venueId, String ownerEmail, SlotRequest request) {
        Venue venue = getOwnedVenue(venueId, ownerEmail);

        Slot slot = Slot.builder()
                .venue(venue)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(Slot.SlotStatus.AVAILABLE)
                .build();

        return slotRepository.save(slot);
    }

    // Delete a slot (only if not booked)
    @Transactional
    public void deleteSlot(Long venueId, Long slotId, String ownerEmail) {
        getOwnedVenue(venueId, ownerEmail); // verify ownership

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.getStatus() == Slot.SlotStatus.BOOKED) {
            throw new IllegalStateException("Cannot delete a booked slot!");
        }

        slotRepository.delete(slot);
    }

    // Get all bookings for this owner's venues
    public List<Booking> getMyVenueBookings(String ownerEmail) {
        User owner = getOwner(ownerEmail);
        List<Venue> myVenues = venueRepository.findByOwnerId(owner.getId());

        return myVenues.stream()
                .flatMap(v -> bookingRepository.findByVenueId(v.getId()).stream())
                .collect(Collectors.toList());
    }
}
