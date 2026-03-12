package com.sportslot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private Long venueId;

    @Min(1) @Max(5)
    @NotNull(message = "Rating must be between 1 and 5")
    private Integer rating;

    private String comment;
}
