package com.medici.app.utils;

import com.medici.app.model.EntityType;
import com.medici.app.model.Review;
import com.medici.app.model.ReviewSource;
import com.medici.app.model.ReviewStatus;

import java.time.Instant;
import java.util.UUID;

import static com.medici.app.utils.ConstantTestUtils.*;

public class ReviewTestUtils {

    public static Review getReview() {
        return getReview(UUID.randomUUID(), TITLE, COMMENT, RATING_5);
    }

    public static Review getReview(UUID userID) {
        return getReview(userID, TITLE, COMMENT, RATING_1);
    }

    public static Review getReview(UUID userID, String title) {
        return getReview(userID, title, COMMENT, RATING_1);
    }

    public static Review getReview(UUID userID, String title, String comment) {
        return getReview(userID, title, comment, RATING_1);
    }

    public static Review getReview(UUID userID, String title, String comment, int rating) {
        Review review = new Review(
                UUID.randomUUID(),
                UUID.randomUUID(),
                EntityType.PRODUCT,
                userID,
                5,
                title,
                comment,
                ReviewStatus.APPROVED,
                ReviewSource.GOOGLE,
                Instant.now(),
                null);
        return review;
    }
}