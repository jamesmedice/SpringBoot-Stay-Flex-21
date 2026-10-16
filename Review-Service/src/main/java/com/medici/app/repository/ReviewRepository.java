package com.medici.app.repository;

import com.medici.app.entity.Review;
import com.medici.app.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByUserId(UUID userID);
    List<Review> findByStatus(ReviewStatus status);

}
