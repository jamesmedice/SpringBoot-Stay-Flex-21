package com.medici.app.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medici.app.entity.Account;
import com.medici.app.entity.AccountRole;
import com.medici.app.entity.AccountUser;
import com.medici.app.entity.AccountUserStatus;
import com.medici.app.model.AccountDtos;
import com.medici.app.repository.AccountRepository;
import com.medici.app.repository.AccountUserRepository;
import com.medici.app.repository.AuthorizationRepository;
import com.medici.app.utils.PaymentUrlParam;
import com.medici.app.utils.Payment_URLS;
import com.medici.app.utils.UrlBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import static com.medici.app.exceptions.LogExceptionMessages.TIMED_OUT_WAITING_TO_START;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AuthorizationRepository authorizationRepository;

    @Autowired
    AccountUserRepository accountUserRepository;

    @Autowired
    AccountRepository accountRepository;


    @BeforeEach
    void init() {
        authorizationRepository.deleteAll();
        accountUserRepository.deleteAll();

        Account account = accountRepository.findById(ACCOUNT_ID).orElseThrow();

        account.setAvailableBalance(new BigDecimal("1000.00"));
        //   account.setVersion(0);
        accountRepository.save(account);

        Account secondAccount = accountRepository.findById(SECOND_ACCOUNT_ID).orElseThrow();
        secondAccount.setAvailableBalance(new BigDecimal("50.00"));
        //   secondAccount.setVersion(0);
        accountRepository.save(secondAccount);

        accountUserRepository.save(AccountUser.create(
                ACCOUNT_ID,
                OWNER_ID,
                AccountRole.OWNER,
                AccountUserStatus.ACTIVE
        ));

        accountUserRepository.save(AccountUser.create(
                ACCOUNT_ID,
                MEMBER_ID,
                AccountRole.MEMBER,
                AccountUserStatus.ACTIVE
        ));

        accountUserRepository.save(AccountUser.create(
                SECOND_ACCOUNT_ID,
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                AccountRole.OWNER,
                AccountUserStatus.ACTIVE
        ));
    }

    private static final UUID ACCOUNT_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID SECOND_ACCOUNT_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID OWNER_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private static final UUID MEMBER_ID =
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    private static final UUID OTHER_USER_ID =
            UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");


    @Order(1)
    @Test
    void createAccount_returns201AndAccountResponse() throws Exception {
        Map<String, Object> request = Map.of(
                "ownerId", OTHER_USER_ID
        );

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.BASE_PAYMENT.getUrl(), null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertNotNull(body.get("id"));
        assertEquals(0, body.get("availableBalance").decimalValue().compareTo(BigDecimal.ZERO));
    }

    @Order(2)
    @Test
    void getAccount_returns200AndSeededAccount() throws Exception {

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.FIND_ACCOUNT_BY_ID.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(ACCOUNT_ID.toString(), body.get("id").asText());
        assertEquals(0, body.get("availableBalance").decimalValue()
                .compareTo(new BigDecimal("1000.00")));
    }

    @Order(3)
    @Test
    void getAccount_whenMissing_returns404() {
        UUID missingId = UUID.randomUUID();

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.FIND_ACCOUNT_BY_ID.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), missingId), null);

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Order(4)
    @Test
    void addMember_returns201() throws Exception {
        AccountDtos.AddAccountMemberRequest request = new AccountDtos.AddAccountMemberRequest(OTHER_USER_ID, AccountRole.MEMBER);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.ADD_MEMBERS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(ACCOUNT_ID.toString(), body.get("accountId").asText());
        assertEquals(OTHER_USER_ID.toString(), body.get("userId").asText());
        assertEquals("MEMBER", body.get("role").asText());
        assertEquals("ACTIVE", body.get("status").asText());
    }

    @Order(5)
    @Test
    void addMember_whenAlreadyAssociated_returns409() {
        AccountDtos.AddAccountMemberRequest request = new AccountDtos.AddAccountMemberRequest(MEMBER_ID, AccountRole.MEMBER);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.ADD_MEMBERS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Order(6)
    @Test
    void depositWithSql_returns201AndUpdatedBalance() throws Exception {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("100.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(0, body.get("amount").decimalValue().compareTo(new BigDecimal("100.00")));
    }

    @Order(7)
    @Test
    void depositWithJpa_returns201AndUpdatedBalance() throws Exception {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("75.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT_JPA.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), SECOND_ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(0, body.get("availableBalance").decimalValue().compareTo(new BigDecimal("125.00")));
    }

    @Order(8)
    @Test
    void depositWithSql_whenAmountIsNonPositive_returns404() {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("15.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), UUID.randomUUID().toString()), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Order(9)
    @Test
    void depositWithSql_whenAmountIsNonPositive_returns400() {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("-15.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Order(10)
    @Test
    void authorize_whenEnoughFunds_returnsApproved() throws Exception {
        AccountDtos.AuthorizePaymentRequest request = new AccountDtos.AuthorizePaymentRequest(MEMBER_ID, new BigDecimal("100.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals("APPROVED", body.get("status").asText());
        assertEquals(ACCOUNT_ID.toString(), body.get("accountId").asText());
        assertEquals(MEMBER_ID.toString(), body.get("cardholderId").asText());
    }

    @Order(11)
    @Test
    void authorize_whenInsufficientFunds_returnsDeclined() throws Exception {
        AccountDtos.AuthorizePaymentRequest request = new AccountDtos.AuthorizePaymentRequest(MEMBER_ID, new BigDecimal("2000.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DECLINED", json(response).get("status").asText());
    }

    @Order(12)
    @Test
    void authorize_whenCardholderIsNotMember_returns403() {
        AccountDtos.AuthorizePaymentRequest request = new AccountDtos.AuthorizePaymentRequest(OTHER_USER_ID, new BigDecimal("10.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Order(13)
    @Test
    void authorize_retryWithSameKey_returnsSameAuthorization() throws Exception {
        AccountDtos.AuthorizePaymentRequest request = new AccountDtos.AuthorizePaymentRequest(MEMBER_ID, new BigDecimal("100.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> first = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        ResponseEntity<String> second = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.OK, first.getStatusCode());
        assertEquals(HttpStatus.OK, second.getStatusCode());

        JsonNode firstBody = json(first);
        JsonNode secondBody = json(second);

        assertEquals(
                firstBody.get("authorizationId").asText(),
                secondBody.get("authorizationId").asText()
        );
    }

    @Order(14)
    @Test
    void authorize_reusingKeyForDifferentAmount_returns409() {
        String idempotencyKey = UUID.randomUUID().toString();

        AccountDtos.AuthorizePaymentRequest firstRequest = new AccountDtos.AuthorizePaymentRequest(MEMBER_ID, new BigDecimal("100.00"), idempotencyKey);

        AccountDtos.AuthorizePaymentRequest secondRequest = new AccountDtos.AuthorizePaymentRequest(MEMBER_ID, new BigDecimal("200.00"), idempotencyKey);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        ResponseEntity<String> first = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(firstRequest),
                String.class
        );

        ResponseEntity<String> second = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(secondRequest),
                String.class
        );

        assertEquals(HttpStatus.OK, first.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, second.getStatusCode());
    }

    @Order(15)
    @Test
    void authorize_whenTwoPaymentsRunInParallel_doesNotOverdrawAccount() throws Exception {

        BigDecimal amount = new BigDecimal("700.00");

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        // Different idempotency keys represent two different payments.
        var request1 = new AccountDtos.AuthorizePaymentRequest(
                MEMBER_ID,
                amount,
                UUID.randomUUID().toString()
        );


        var request2 = new AccountDtos.AuthorizePaymentRequest(
                MEMBER_ID,
                amount,
                UUID.randomUUID().toString()
        );

        var request3 = new AccountDtos.AuthorizePaymentRequest(
                MEMBER_ID,
                amount,
                UUID.randomUUID().toString()
        );


        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch ready = new CountDownLatch(3);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Callable<ResponseEntity<String>> payment1 = () -> {
                ready.countDown();

                if (!start.await(10, TimeUnit.SECONDS))
                    throw new IllegalStateException(TIMED_OUT_WAITING_TO_START);

                return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request1),
                        String.class
                );
            };

            Callable<ResponseEntity<String>> payment2 = () -> {
                ready.countDown();
                if (!start.await(10, TimeUnit.SECONDS))
                    throw new IllegalStateException(TIMED_OUT_WAITING_TO_START);

                return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request2),
                        String.class
                );
            };

            Callable<ResponseEntity<String>> payment3 = () -> {
                ready.countDown();
                if (!start.await(10, TimeUnit.SECONDS))
                    throw new IllegalStateException(TIMED_OUT_WAITING_TO_START);

                return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request3),
                        String.class
                );
            };

            Future<ResponseEntity<String>> future1 = executor.submit(payment1);
            Future<ResponseEntity<String>> future2 = executor.submit(payment2);
            Future<ResponseEntity<String>> future3 = executor.submit(payment3);

            // Wait until both threads are ready, then release them together.
            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();

            ResponseEntity<String> response1 = future1.get(15, TimeUnit.SECONDS);

            ResponseEntity<String> response2 = future2.get(15, TimeUnit.SECONDS);

            ResponseEntity<String> response3 = future3.get(15, TimeUnit.SECONDS);

            // Both requests are processed successfully at the HTTP level.
            assertEquals(HttpStatus.OK, response1.getStatusCode());
            assertEquals(HttpStatus.OK, response2.getStatusCode());
            assertEquals(HttpStatus.OK, response3.getStatusCode());

            String status1 = json(response1).get("status").asText();
            String status2 = json(response2).get("status").asText();
            String status3 = json(response3).get("status").asText();

            List<String> statuses = List.of(status1, status2, status3);

            assertEquals(
                    1,
                    statuses.stream().filter("APPROVED"::equals).count()
            );

            assertEquals(
                    2,
                    statuses.stream().filter("DECLINED"::equals).count()
            );

            // Verify the actual persisted database state.
            // JdbcTemplate reads directly from the database.
            Account account = accountRepository.findById(ACCOUNT_ID).orElseThrow(AssertionError::new);
            BigDecimal finalBalance = account.getAvailableBalance();
            long finalVersion = account.getVersion();

            assertEquals(0, finalBalance.compareTo(new BigDecimal("300.00")));
            assertEquals(true, finalVersion > 1);

            // Both decisions must be persisted, including the decline.
            assertEquals(
                    3,
                    authorizationRepository.findAll().stream()
                            .filter(a -> a.getAccountId().equals(ACCOUNT_ID))
                            .count()
            );

        } finally {
            // Always release waiting threads and shut down the executor.
            start.countDown();
            executor.shutdownNow();
        }
    }

    @Order(16)
    @Test
    void authorize_whenParallelPaymentsAreRetried_doesNotDebitTwice() throws Exception {

        BigDecimal amount = new BigDecimal("300.00");

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), ACCOUNT_ID), null);

        // Two independent payments, each with its own idempotency key.
        var request1 = new AccountDtos.AuthorizePaymentRequest(
                MEMBER_ID,
                amount,
                UUID.randomUUID().toString()
        );

        var request2 = new AccountDtos.AuthorizePaymentRequest(
                MEMBER_ID,
                amount,
                UUID.randomUUID().toString()
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Callable<ResponseEntity<String>> payment1 = () -> {
                ready.countDown();

                if (!start.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("TIMED_OUT_WAITING_TO_START");
                }

                return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request1),
                        String.class
                );
            };

            Callable<ResponseEntity<String>> payment2 = () -> {
                ready.countDown();

                if (!start.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("TIMED_OUT_WAITING_TO_START");
                }

                return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request2),
                        String.class
                );
            };

            Future<ResponseEntity<String>> future1 = executor.submit(payment1);
            Future<ResponseEntity<String>> future2 = executor.submit(payment2);

            // Release both requests together.
            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();

            ResponseEntity<String> response1 = future1.get(15, TimeUnit.SECONDS);

            ResponseEntity<String> response2 = future2.get(15, TimeUnit.SECONDS);

            assertEquals(HttpStatus.OK, response1.getStatusCode());
            assertEquals(HttpStatus.OK, response2.getStatusCode());

            assertEquals("APPROVED", json(response1).get("status").asText());
            assertEquals("APPROVED", json(response2).get("status").asText());

            // Retry payment 1 using the EXACT same request and idempotency key.
            ResponseEntity<String> retryResponse = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    jsonRequest(request1),
                    String.class
            );

            assertEquals(HttpStatus.OK, retryResponse.getStatusCode());
            assertEquals(
                    "APPROVED",
                    json(retryResponse).get("status").asText()
            );

            // The retry must return the original authorization.
            assertEquals(
                    json(response1).get("authorizationId").asText(),
                    json(retryResponse).get("authorizationId").asText()
            );

            // Both payments debit the account, but the retry does not.
            Account account = accountRepository.findById(ACCOUNT_ID)
                    .orElseThrow(AssertionError::new);

            assertEquals(
                    0,
                    account.getAvailableBalance()
                            .compareTo(new BigDecimal("400.00"))
            );

            // Only two authorization records should exist.
            assertEquals(
                    2,
                    authorizationRepository.findAll().stream()
                            .filter(a -> a.getAccountId().equals(ACCOUNT_ID))
                            .count()
            );

        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }

    @Order(17)
    @Test
    void accountLifecycle_createDepositAddMemberAndPay() throws Exception {
        // Arrange: identities and amounts
        UUID ownerId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        BigDecimal depositAmount = new BigDecimal("1000.00");
        BigDecimal ownerPaymentAmount = new BigDecimal("200.00");
        BigDecimal memberPaymentAmount = new BigDecimal("300.00");

        // 1. Create an account. The owner should be created automatically.
        AccountDtos.CreateAccountRequest createRequest = new AccountDtos.CreateAccountRequest(ownerId);

        String createAccountUrl = UrlBuilderUtils.buildUrl(Payment_URLS.BASE_PAYMENT.getUrl(), null, null);

        ResponseEntity<String> createResponse = restTemplate.exchange(
                createAccountUrl,
                HttpMethod.POST,
                jsonRequest(createRequest),
                String.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        JsonNode createdAccountJson = json(createResponse);
        UUID accountId = UUID.fromString(
                createdAccountJson.get("id").asText()
        );

        assertEquals(
                0,
                new BigDecimal(
                        createdAccountJson.get("availableBalance").asText()
                ).compareTo(BigDecimal.ZERO)
        );

        // 2. Deposit money into the newly created account.
        AccountDtos.DepositRequest depositRequest = new AccountDtos.DepositRequest(
                depositAmount,
                UUID.randomUUID().toString()
        );

        String depositUrl = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT_JPA.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> depositResponse = restTemplate.exchange(
                depositUrl,
                HttpMethod.PUT,
                jsonRequest(depositRequest),
                String.class
        );

        assertEquals(HttpStatus.CREATED, depositResponse.getStatusCode());

        // 3. Add a member to the account.
        AccountDtos.AddAccountMemberRequest addMemberRequest = new AccountDtos.AddAccountMemberRequest(memberId, AccountRole.MEMBER);

        String addMemberUrl = UrlBuilderUtils.buildUrl(Payment_URLS.ADD_MEMBERS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> addMemberResponse = restTemplate.exchange(
                addMemberUrl,
                HttpMethod.PUT,
                jsonRequest(addMemberRequest),
                String.class
        );

        assertEquals(HttpStatus.CREATED, addMemberResponse.getStatusCode());

        // 4. Owner makes a payment.
        AccountDtos.AuthorizePaymentRequest ownerPayment = new AccountDtos.AuthorizePaymentRequest(
                ownerId,
                ownerPaymentAmount,
                UUID.randomUUID().toString()
        );

        String authorizationUrl = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> ownerPaymentResponse = restTemplate.exchange(
                authorizationUrl,
                HttpMethod.PUT,
                jsonRequest(ownerPayment),
                String.class
        );

        assertEquals(HttpStatus.OK, ownerPaymentResponse.getStatusCode());
        assertEquals("APPROVED", json(ownerPaymentResponse).get("status").asText());

        // 5. Member makes a payment.
        AccountDtos.AuthorizePaymentRequest memberPayment = new AccountDtos.AuthorizePaymentRequest(
                memberId,
                memberPaymentAmount,
                UUID.randomUUID().toString()
        );

        ResponseEntity<String> memberPaymentResponse = restTemplate.exchange(
                authorizationUrl,
                HttpMethod.PUT,
                jsonRequest(memberPayment),
                String.class
        );

        assertEquals(HttpStatus.OK, memberPaymentResponse.getStatusCode());
        assertEquals("APPROVED", json(memberPaymentResponse).get("status").asText());

        // 6. Retry the owner's exact request using the same idempotency key.
        ResponseEntity<String> ownerRetryResponse = restTemplate.exchange(
                authorizationUrl,
                HttpMethod.PUT,
                jsonRequest(ownerPayment),
                String.class
        );

        assertEquals(HttpStatus.OK, ownerRetryResponse.getStatusCode());
        assertEquals("APPROVED", json(ownerRetryResponse).get("status").asText());

        // A retry must return the original authorization, not create a new one.
        assertEquals(
                json(ownerPaymentResponse).get("authorizationId").asText(),
                json(ownerRetryResponse).get("authorizationId").asText()
        );

        // 7. Verify the final persisted state.
        Account account = accountRepository.findById(accountId).orElseThrow(AssertionError::new);

        assertEquals(
                0,
                account.getAvailableBalance()
                        .compareTo(new BigDecimal("500.00"))
        );

        assertEquals(3, account.getVersion());

        assertTrue(accountUserRepository.existsByIdAccountIdAndIdUserIdAndStatus(accountId, ownerId, AccountUserStatus.ACTIVE));

        assertTrue(accountUserRepository.existsByIdAccountIdAndIdUserIdAndStatus(accountId, memberId, AccountUserStatus.ACTIVE));

        assertEquals(
                2,
                authorizationRepository
                        .findAll()
                        .stream()
                        .filter(a -> a.getAccountId().equals(accountId))
                        .count()
        );
    }

    private HttpEntity<Object> jsonRequest(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Object> jsonRequest(Record body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private JsonNode json(ResponseEntity<String> response) throws Exception {
        return objectMapper.readTree(response.getBody());
    }
}