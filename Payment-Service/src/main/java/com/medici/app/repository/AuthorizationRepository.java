package com.medici.app.repository;

import com.medici.app.entity.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AuthorizationRepository extends JpaRepository<Authorization, UUID> {

    Optional<Authorization> findByAccountIdAndIdempotencyKey(UUID accountId, String idempotencyKey);

    long countByAccountId(UUID accountId);
}