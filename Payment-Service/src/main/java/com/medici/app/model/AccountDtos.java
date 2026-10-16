package com.medici.app.model;

import com.medici.app.entity.AccountRole;
import com.medici.app.entity.Authorization;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public final class AccountDtos {

    private AccountDtos() {
    }

    public enum AuthorizationStatus {
        APPROVED,
        DECLINED
    }

    public enum DepositStatus {
        SUCCESS,
        FAILED,
    }

    public record CreateAccountRequest(
            @NotNull UUID ownerId
    ) {
    }

    public record AddAccountMemberRequest(
            @NotNull UUID userId,
            @NotNull AccountRole role
    ) {
    }

    public record DepositRequest(
            @NotNull
            @DecimalMin(value = "0.01")
            @Digits(integer = 17, fraction = 2)
            BigDecimal amount,

            @NotBlank
            @Size(max = 150)
            String idempotencyKey
    ) {
    }

    public record DepositResponse(
            UUID accountId,
            BigDecimal amount,
            String idempotencyKey,
            DepositStatus status
    ) {
    }

    public record AuthorizePaymentRequest(
            @NotNull UUID cardholderId,

            @NotNull
            @DecimalMin(value = "0.01")
            @Digits(integer = 17, fraction = 2)
            BigDecimal amount,

            @NotBlank
            @Size(max = 150)
            String idempotencyKey
    ) {
    }

    public record AccountResponse(
            UUID id,
            BigDecimal availableBalance,
            long version
    ) {
    }

    public record AccountMemberResponse(
            UUID accountId,
            UUID userId,
            String role,
            String status
    ) {
    }

    public record AuthorizationResponse(
            UUID authorizationId,
            UUID accountId,
            UUID cardholderId,
            BigDecimal amount,
            AuthorizationStatus status
    ) {
    }

    public static AuthorizationResponse toResponse(Authorization authorization) {
        return new AuthorizationResponse(
                authorization.getId(),
                authorization.getAccountId(),
                authorization.getCardholderId(),
                authorization.getAmount(),
                authorization.getStatus()
        );
    }
}
