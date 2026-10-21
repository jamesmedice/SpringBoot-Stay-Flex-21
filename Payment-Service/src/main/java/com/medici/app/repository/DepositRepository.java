package com.medici.app.repository;

import com.medici.app.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {

    @Deprecated
    @Modifying
    @Query(value = """
            INSERT INTO FINANCE.T_DEPOSIT (
                id,
                account_id,
                idempotency_key,
                amount
            )
            VALUES (
                :id,
                :accountId,
                :idempotencyKey,
                :amount
            )
            ON CONFLICT (account_id, idempotency_key)
            DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsentNative(
            @Param("id") UUID id,
            @Param("accountId") UUID accountId,
            @Param("idempotencyKey") String idempotencyKey,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query(value = """
            INSERT INTO Deposit (
                id,
                accountId,
                idempotencyKey,
                amount
            )
            VALUES (
                :id,
                :accountId,
                :idempotencyKey,
                :amount
            )
            """)
    int insertIfAbsent(
            @Param("id") UUID id,
            @Param("accountId") UUID accountId,
            @Param("idempotencyKey") String idempotencyKey,
            @Param("amount") BigDecimal amount
    );

    Optional<Deposit> findByAccountIdAndIdempotencyKey(UUID accountId, String idempotencyKey);

    boolean existsByAccountIdAndIdempotencyKey(UUID accountId, String idempotencyKey);
}