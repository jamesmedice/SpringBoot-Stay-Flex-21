package com.medici.app.service;

import com.medici.app.model.EntityType;
import com.medici.app.model.Review;
import com.medici.app.model.ReviewStatus;
import com.medici.app.repository.ReviewRepository;
import com.medici.app.utils.ReviewUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    public void save(Review review) {
        com.medici.app.entity.Review entity = ReviewUtils.modelToNewEntity.apply(review);
        reviewRepository.save(entity);
    }

    public List<Review> findByUserId(final UUID userId) {
        List<com.medici.app.entity.Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream().map(entity -> ReviewUtils.entityToModel.apply(entity)).toList();
    }

    public List<Review> findByStatus(final ReviewStatus status) {
        List<com.medici.app.entity.Review> reviews = reviewRepository.findByStatus(status);
        return reviews.stream().map(entity -> ReviewUtils.entityToModel.apply(entity)).toList();
    }

    public void update(final UUID uuid, final @Valid Review review) {
        com.medici.app.entity.Review entity = findById(uuid);
        entity.setEntityType(EntityType.PRODUCT);
        entity.setRating(review.rating());
        entity.setTitle(review.title());
        entity.setComment(review.comment());
        entity.setStatus(review.status());
        entity.setSource(review.source());
        entity.setUpdatedAt(Instant.now());

        reviewRepository.save(entity);
    }

    private com.medici.app.entity.Review findById(final UUID uuid) {
        Optional<com.medici.app.entity.Review> entity = reviewRepository.findById(uuid);

        if (entity.isEmpty())
            throw new EntityNotFoundException();

        return entity.get();
    }

    public void delete(final UUID uuid) {
        reviewRepository.deleteById(uuid);
    }

    public Slice<Review> findAll(final Pageable pageable) {
        Page<com.medici.app.entity.Review> reviews = reviewRepository.findAll(pageable);
        return reviews.map(review -> ReviewUtils.entityToModel.apply(review));
    }
}