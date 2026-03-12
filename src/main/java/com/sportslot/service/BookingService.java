package com.sportslot.service;

import com.sportslot.dto.request.BookingRequest;
import com.sportslot.entity.*;
import com.sportslot.exception.ResourceNotFoundException;
import com.sportslot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final SlotRepository slotRepository;

    @Transactional
    public Booking createBooking(String userEmail, BookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.getStatus() == Slot.SlotStatus.BOOKED)
            throw new IllegalStateException("This slot is already booked!");

        slot.setStatus(Slot.SlotStatus.BOOKED);
        slotRepository.save(slot);

        Booking booking = Booking.builder()
                .user(user).venue(venue).slot(slot)
                .totalPrice(venue.getPricePerHour())
                .status(Booking.BookingStatus.CONFIRMED)
                .build();
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return bookingRepository.findByUserId(user.getId());
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (!booking.getUser().getEmail().equals(userEmail))
            throw new SecurityException("You can only cancel your own bookings");

        booking.getSlot().setStatus(Slot.SlotStatus.AVAILABLE);
        slotRepository.save(booking.getSlot());
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}
