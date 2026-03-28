package com.sportslot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VenueRequest {

    @NotBlank(message = "Venue name is required")
    private String name;

    @NotBlank(message = "Sport is required")
    private String sport;

    @NotBlank(message = "Location is required")
    private String location;

    private String address;
    private String description;

    @NotNull(message = "Price per hour is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double pricePerHour;

    private String amenities;
}
