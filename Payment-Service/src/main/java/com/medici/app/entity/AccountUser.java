package com.medici.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "T_ACCOUNT_USER", schema = "FINANCE")
public class AccountUser {

    @EmbeddedId
    private AccountUserId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private AccountRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountUserStatus status = AccountUserStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Factory method
    public static AccountUser create(
            UUID accountId,
            UUID userId,
            AccountRole role,
            AccountUserStatus status) {

        AccountUser accountUser = new AccountUser();
        accountUser.id = new AccountUserId(accountId, userId);
        accountUser.role = role;
        accountUser.status = status;
        accountUser.createdAt = Instant.now();
        return accountUser;
    }

}