package com.medici.app.repository;

import com.medici.app.entity.AccountUser;
import com.medici.app.entity.AccountUserId;
import com.medici.app.entity.AccountUserStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountUserRepository extends JpaRepository<AccountUser, AccountUserId> {

    List<AccountUser> findByIdAccountId(UUID accountId);

    List<AccountUser> findByIdUserId(UUID userId);

    Optional<AccountUser> findByIdAccountIdAndIdUserId(UUID accountId, UUID userId);

    boolean existsByIdAccountIdAndIdUserId(UUID accountId, UUID userId);

    boolean existsByIdAccountIdAndIdUserIdAndStatus(UUID accountId, UUID userId, AccountUserStatus status);
}