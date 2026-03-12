package com.sportslot.repository;

import com.sportslot.entity.Slot;
import com.sportslot.entity.Slot.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByVenueIdAndDate(Long venueId, LocalDate date);
    List<Slot> findByVenueIdAndDateAndStatus(Long venueId, LocalDate date, SlotStatus status);
}
