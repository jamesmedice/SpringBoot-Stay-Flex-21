package com.medici.app.service;

import com.medici.app.entity.AccountUserStatus;
import com.medici.app.model.AccountDtos.AuthorizationResponse;
import com.medici.app.model.AccountDtos.AuthorizePaymentRequest;
import com.medici.app.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.CARDHOLDER_IS_NOT_AN_ACTIVE_MEMBER_OF_THIS_ACCOUNT;

@Service
@RequiredArgsConstructor
public class PaymentAuthorizationService {

    private final AccountUserRepository accountUserRepository;
    private final PaymentAuthorizationTransactionService transactionService;

    public AuthorizationResponse authorize(UUID accountId, AuthorizePaymentRequest request) {
        boolean isMember = accountUserRepository.existsByIdAccountIdAndIdUserIdAndStatus(
                accountId,
                request.cardholderId(),
                AccountUserStatus.ACTIVE
        );

        if (!isMember)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, CARDHOLDER_IS_NOT_AN_ACTIVE_MEMBER_OF_THIS_ACCOUNT);

        return transactionService.authorize(accountId, request);
    }
}