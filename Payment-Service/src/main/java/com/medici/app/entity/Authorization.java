package com.medici.app.entity;

import com.medici.app.model.AccountDtos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_PAYMENT_AUTHORIZATION", schema = "FINANCE", uniqueConstraints =
@UniqueConstraint(name = "uq_authorization_idempotency", columnNames = {"account_id", "idempotency_key"}))
public class Authorization {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "cardholder_id", nullable = false)
    private UUID cardholderId;

    @Column(name = "idempotency_key", nullable = false, length = 150)
    private String idempotencyKey;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountDtos.AuthorizationStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}