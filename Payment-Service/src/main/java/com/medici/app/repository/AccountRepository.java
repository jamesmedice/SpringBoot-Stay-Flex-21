package com.medici.app.repository;

import com.medici.app.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.*;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") UUID id);

    @Modifying
    @Query(value = """
            UPDATE Account SET availableBalance = availableBalance - :amount, version = version + 1
            WHERE id = :id and availableBalance >= :amount
            """)
    int debitIfEnough(@Param("id") UUID id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query(value = """
            UPDATE Account SET availableBalance = availableBalance + :amount, version = version + 1
             WHERE id = :id
            """)
    int credit(@Param("id") UUID id, @Param("amount") BigDecimal amount);
}