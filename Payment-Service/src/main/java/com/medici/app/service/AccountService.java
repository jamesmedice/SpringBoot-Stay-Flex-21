package com.medici.app.service;

import com.medici.app.entity.Account;
import com.medici.app.entity.AccountRole;
import com.medici.app.entity.AccountUser;
import com.medici.app.entity.AccountUserStatus;
import com.medici.app.model.AccountDtos;
import com.medici.app.model.AccountDtos.*;
import com.medici.app.repository.AccountRepository;
import com.medici.app.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountUserRepository accountUserRepository;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        UUID accountId = UUID.randomUUID();

        Account account = new Account(accountId, BigDecimal.ZERO);
        accountRepository.save(account);

        AccountUser owner = AccountUser.create(
                accountId,
                request.ownerId(),
                AccountRole.OWNER,
                AccountUserStatus.ACTIVE
        );

        accountUserRepository.save(owner);

        return toAccountResponse(account);
    }

    @Transactional
    public AccountMemberResponse addMember(UUID accountId, AddAccountMemberRequest request) {

        Account account = findAccountOrThrow(accountId);
        UUID userId = request.userId();

        if (accountUserRepository.existsByIdAccountIdAndIdUserId(accountId, userId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, USER_IS_ALREADY_ASSOCIATED_WITH_THIS_ACCOUNT);

        AccountUser member = AccountUser.create(
                accountId,
                userId,
                AccountRole.MEMBER,
                AccountUserStatus.ACTIVE
        );

        accountUserRepository.save(member);

        return new AccountMemberResponse(
                account.getId(),
                userId,
                AccountRole.MEMBER.name(),
                AccountUserStatus.ACTIVE.name()
        );
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID accountId) {
        return toAccountResponse(findAccountOrThrow(accountId));
    }

    @Transactional
    public AccountResponse depositWithJpa(UUID accountId, DepositRequest request) {
        Account account = accountRepository.findByIdForUpdate(accountId).orElseThrow(() -> accountNotFound(accountId));

        account.credit(request.amount());
        accountRepository.flush();

        return new AccountResponse(
                account.getId(),
                account.getAvailableBalance(),
                account.getVersion()
        );
    }

    @Transactional
    public DepositResponse depositWithSql(UUID accountId, DepositRequest request) {
        validateDepositAmount(request.amount());

        int updated = accountRepository.credit(accountId, request.amount());

        if (updated == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageFormat.format(ACCOUNT_NOT_FOUND, accountId));

        AccountDtos.DepositStatus status = updated == 1 ? DepositStatus.SUCCESS : DepositStatus.FAILED;

        return new DepositResponse(
                accountId,
                request.amount(),
                request.idempotencyKey(),
                status
        );
    }

    private void validateDepositAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DEPOSIT_AMOUNT_MUST_BE_POSITIVE);
    }

    private Account findAccountOrThrow(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> accountNotFound(accountId));
    }

    private ResponseStatusException accountNotFound(UUID accountId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, MessageFormat.format(ACCOUNT_NOT_FOUND, accountId));
    }

    private AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAvailableBalance(),
                account.getVersion()
        );
    }
}