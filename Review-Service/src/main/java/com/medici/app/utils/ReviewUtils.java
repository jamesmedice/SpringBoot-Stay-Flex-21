package com.medici.app.utils;

import com.medici.app.model.Review;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewUtils {

    public static Function<com.medici.app.entity.Review, Review> entityToModel = entity ->
            new Review(
                    entity.getId(),
                    entity.getEntityId(),
                    entity.getEntityType(),
                    entity.getUserId(),
                    entity.getRating(),
                    entity.getTitle(),
                    entity.getComment(),
                    entity.getStatus(),
                    entity.getSource(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
            );

    public static Function<Review, com.medici.app.entity.Review> modelToEntity = model ->
            new com.medici.app.entity.Review(
                    model.id(),
                    model.entityId(),
                    model.entityType(),
                    model.userId(),
                    model.rating(),
                    model.title(),
                    model.comment(),
                    model.status(),
                    model.source(),
                    model.createdAt(),
                    model.updatedAt()
            );

    public static Function<Review, com.medici.app.entity.Review> modelToNewEntity = model ->
            new com.medici.app.entity.Review(
                    UUID.randomUUID(),
                    model.entityId(),
                    model.entityType(),
                    model.userId(),
                    model.rating(),
                    model.title(),
                    model.comment(),
                    model.status(),
                    model.source(),
                    Instant.now(),
                    null
            );
}