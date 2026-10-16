package com.medici.app.resource;

import com.medici.app.model.AccountDtos.*;
import com.medici.app.service.AccountService;
import com.medici.app.service.PaymentAuthorizationService;
import com.medici.app.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/payment/accounts")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final AccountService accountService;

    private final PaymentService paymentService;

    private final PaymentAuthorizationService paymentAuthorizationService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<AccountMemberResponse> addMember(@PathVariable @NotNull UUID id, @Valid @RequestBody AddAccountMemberRequest request) {
        AccountMemberResponse response = accountService.addMember(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PutMapping("/{id}/deposits")
    public ResponseEntity<DepositResponse> depositAlpha(@PathVariable @NotNull UUID id, @Valid @RequestBody DepositRequest request) {
        DepositResponse response = accountService.depositWithSql(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/deposits/acid")
    public ResponseEntity<AccountResponse> depositBeta(@PathVariable @NotNull UUID id, @Valid @RequestBody DepositRequest request) {
        AccountResponse response = accountService.depositWithJpa(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/authorizations")
    public ResponseEntity<AuthorizationResponse> authorizePayment(@PathVariable @NotNull UUID id, @Valid @RequestBody AuthorizePaymentRequest request) {
        AuthorizationResponse response = paymentService.authorize(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/authorizationx")
    public ResponseEntity<AuthorizationResponse> authorizePaymentx(@PathVariable @NotNull UUID id, @Valid @RequestBody AuthorizePaymentRequest request) {
        AuthorizationResponse response = paymentAuthorizationService.authorize(id, request);
        return ResponseEntity.ok(response);
    }
}
