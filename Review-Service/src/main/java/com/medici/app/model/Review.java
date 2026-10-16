package com.medici.app.model;

import java.time.Instant;
import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.RATING_MUST_BE_BETWEEN_1_AND_5;

public record Review(
        UUID id,
        UUID entityId,
        EntityType entityType,
        UUID userId,
        int rating,
        String title,
        String comment,
        ReviewStatus status,
        ReviewSource source,
        Instant createdAt,
        Instant updatedAt
) {

    public Review {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException(RATING_MUST_BE_BETWEEN_1_AND_5);
    }

}