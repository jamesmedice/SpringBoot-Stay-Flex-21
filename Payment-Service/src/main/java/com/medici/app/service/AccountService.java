package com.medici.app.service;

import com.medici.app.entity.*;
import com.medici.app.exceptions.IdempotencyConflictException;
import com.medici.app.model.AccountDtos.*;
import com.medici.app.repository.AccountRepository;
import com.medici.app.repository.AccountUserRepository;
import com.medici.app.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.medici.app.exceptions.LogExceptionMessages.*;
import static com.medici.app.model.AccountDtos.toResponse;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;
    private final DepositRepository depositRepository;

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
    public DepositResponse depositWithJpa(UUID accountId, DepositRequest request) {
        Account account = accountRepository.findByIdForUpdate(accountId).orElseThrow(() -> accountNotFound(accountId));

        var existing = depositRepository.findByAccountIdAndIdempotencyKey(
                accountId,
                request.idempotencyKey()
        );

        if (existing.isPresent()) {

            if (existing.get().getAmount().compareTo(request.amount()) != 0)
                throw new IdempotencyConflictException();

            return toResponse(existing.get());
        }

        Deposit deposit = new Deposit(
                UUID.randomUUID(),
                accountId,
                request.idempotencyKey(),
                request.amount(),
                LocalDateTime.now()
        );

        depositRepository.saveAndFlush(deposit);

        account.credit(request.amount());
        accountRepository.flush();

        return toResponse(deposit);
    }

    @Transactional
    public DepositResponse depositWithSql(UUID accountId, DepositRequest request) {

        var existing = depositRepository.findByAccountIdAndIdempotencyKey(
                accountId,
                request.idempotencyKey()
        );

        if (existing.isPresent()) {

            if (existing.get().getAmount().compareTo(request.amount()) != 0)
                throw new IdempotencyConflictException();

            return toResponse(existing.get());
        }

        depositRepository.insertIfAbsent(
                UUID.randomUUID(),
                accountId,
                request.idempotencyKey(),
                request.amount()
        );

        int updated = accountRepository.credit(accountId, request.amount());

        if (updated == 0)
            throw accountNotFound(accountId);

        return new DepositResponse(
                accountId,
                request.amount(),
                request.idempotencyKey(),
                DepositStatus.SUCCESS
        );
    }

    private Account findAccountOrThrow(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> accountNotFound(accountId));
    }

    private ResponseStatusException depositNotFound(UUID accountId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, MessageFormat.format(DEPOSIT_NOT_FOUND, accountId));
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