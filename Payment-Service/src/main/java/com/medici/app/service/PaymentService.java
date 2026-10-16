package com.medici.app.service;

import com.medici.app.entity.Authorization;
import com.medici.app.model.AccountDtos;
import com.medici.app.model.AccountDtos.AuthorizationResponse;
import com.medici.app.model.AccountDtos.AuthorizePaymentRequest;
import com.medici.app.repository.AccountRepository;
import com.medici.app.repository.AccountUserRepository;
import com.medici.app.repository.AuthorizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.CARDHOLDER_IS_NOT_AN_ACTIVE_MEMBER_OF_THIS_ACCOUNT;
import static com.medici.app.exceptions.LogExceptionMessages.IDEMPOTENCY_KEY_WAS_ALREADY_USED_FOR_A_DIFFERENT_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {


    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;
    private final AuthorizationRepository authorizationRepository;

    @Transactional
    public AuthorizationResponse authorize(UUID accountId, AuthorizePaymentRequest request) {

        UUID cardholderId = request.cardholderId();
        String idempotencyKey = request.idempotencyKey();

        // 1. Verify that the cardholder is an active account member.
        boolean isMember = accountUserRepository.existsByIdAccountIdAndIdUserIdAndStatus(
                accountId,
                cardholderId,
                com.medici.app.entity.AccountUserStatus.ACTIVE
        );

        if (!isMember)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, CARDHOLDER_IS_NOT_AN_ACTIVE_MEMBER_OF_THIS_ACCOUNT);

        // 2. Handle retries before attempting to debit the account again.
        var existing = authorizationRepository.findByAccountIdAndIdempotencyKey(accountId, idempotencyKey);

        if (existing.isPresent()) {
            Authorization authorization = existing.get();

            // A key cannot be reused for a different payment.
            if (!authorization.getAmount().equals(request.amount()) || !authorization.getCardholderId().equals(cardholderId))
                throw new ResponseStatusException(HttpStatus.CONFLICT, IDEMPOTENCY_KEY_WAS_ALREADY_USED_FOR_A_DIFFERENT_REQUEST);

            return AccountDtos.toResponse(authorization);
        }

        // 3. Atomically reserve funds.
        int updated = accountRepository.debitIfEnough(accountId, request.amount());

        AccountDtos.AuthorizationStatus status = updated == 1 ? AccountDtos.AuthorizationStatus.APPROVED : AccountDtos.AuthorizationStatus.DECLINED;

        // 4. Persist the decision and idempotency key in the same transaction.
        Authorization authorization = new Authorization(
                UUID.randomUUID(),
                accountId,
                cardholderId,
                idempotencyKey,
                request.amount(),
                status,
                Instant.now()
        );

        log.info(authorization.toString());
        authorizationRepository.save(authorization);

        return AccountDtos.toResponse(authorization);
    }

}