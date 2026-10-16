package com.medici.app.entity;

import com.medici.app.exceptions.InsufficientFundsException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "T_ACCOUNT", schema = "FINANCE")
public class Account {

    @Id
    private UUID id;

    @Column(name = "available_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance;

    @Version
    private long version;

    public Account(UUID id, BigDecimal availableBalance) {
        if (id == null) {
            throw new IllegalArgumentException(ACCOUNT_ID_IS_REQUIRED);
        }

        if (availableBalance == null || availableBalance.signum() < 0) {
            throw new IllegalArgumentException(BALANCE_MUST_BE_NON_NEGATIVE);
        }

        this.id = id;
        this.availableBalance = availableBalance;
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(AMOUNT_MUST_BE_POSITIVE);
        }

        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        availableBalance = availableBalance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException(DEPOSIT_AMOUNT_MUST_BE_POSITIVE);

        this.availableBalance = this.availableBalance.add(amount);
    }
}
