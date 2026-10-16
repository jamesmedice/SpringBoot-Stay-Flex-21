package com.medici.app.entity;

import com.medici.app.model.EntityType;
import com.medici.app.model.ReviewSource;
import com.medici.app.model.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "T_REVIEW",
        schema = "FINANCE",
        indexes = {
                @Index(name = "idx_reviews_entity", columnList = "entity_id, entity_type"),
                @Index(name = "idx_reviews_status", columnList = "status"),
                @Index(name = "idx_reviews_user", columnList = "user_id"),
                @Index(name = "idx_reviews_created_at", columnList = "created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_review_entity_user",
                        columnNames = {"entity_id", "user_id"}
                )
        }
)
public class Review {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 32)
    private EntityType entityType;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ReviewStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ReviewSource source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
