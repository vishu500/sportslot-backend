package com.sportslot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "Venue ID is required")
    private Long venueId;

    @NotNull(message = "Slot ID is required")
    private Long slotId;
}
