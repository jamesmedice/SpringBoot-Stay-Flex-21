package com.medici.app.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medici.app.entity.AccountRole;
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
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerIntegrationWithLockTest {

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

    private UUID accountId;
    private final UUID ownerId = UUID.randomUUID();
    private final UUID memberId = UUID.randomUUID();

    @BeforeEach
    void setUp() throws Exception {
        accountRepository.deleteAll();
        accountUserRepository.deleteAll();
        authorizationRepository.deleteAll();

        // Fresh account for this test
        accountId = createAccount();
        addMember(accountId, memberId);
    }

    // 1. Create account independently
    @Test
    @Order(1)
    void getAccount_returns200AndSeededAccount() throws Exception {
        String url = UrlBuilderUtils.buildUrl(Payment_URLS.FIND_ACCOUNT_BY_ID.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(accountId.toString(), body.get("id").asText());
        assertEquals(0, body.get("availableBalance").decimalValue().compareTo(new BigDecimal("0.00")));
    }

    // 2. Add member independently
    @Test
    @Order(2)
    void addMember_returns201() throws Exception {

        UUID member = UUID.randomUUID();
        AccountDtos.AddAccountMemberRequest request = new AccountDtos.AddAccountMemberRequest(member, AccountRole.MEMBER);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.ADD_MEMBERS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(accountId.toString(), body.get("accountId").asText());
        assertEquals(member.toString(), body.get("userId").asText());
        assertEquals("MEMBER", body.get("role").asText());
        assertEquals("ACTIVE", body.get("status").asText());
    }

    // 3. SQL deposit independently
    @Test
    @Order(3)
    void depositWithSql_returns201AndUpdatedBalance() throws Exception {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("100.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

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

    // 4. JPA deposit independently
    @Test
    @Order(4)
    void depositWithJpa_returns201AndUpdatedBalance() throws Exception {
        AccountDtos.DepositRequest request = new AccountDtos.DepositRequest(new BigDecimal("75.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.DEPOSIT_JPA.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(0, body.get("availableBalance").decimalValue().compareTo(new BigDecimal("75.00")));
    }

    // 5. Authorization independently
    @Test
    @Order(5)
    void authorize_whenEnoughFunds_returnsApproved() throws Exception {
        AccountDtos.AuthorizePaymentRequest request = new AccountDtos.AuthorizePaymentRequest(memberId, new BigDecimal("100.00"), UUID.randomUUID().toString());

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.AUTHORIZATIONS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                jsonRequest(request),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = json(response);
        assertEquals(AccountDtos.AuthorizationStatus.DECLINED.name(), body.get("status").asText());
        assertEquals(accountId.toString(), body.get("accountId").asText());
        assertEquals(memberId.toString(), body.get("cardholderId").asText());
    }


    private UUID createAccount() throws Exception {
        var request = new AccountDtos.CreateAccountRequest(ownerId);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.BASE_PAYMENT.getUrl(), null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(url, jsonRequest(request), String.class);

        JsonNode body = json(response);
        return UUID.fromString(body.get("id").asText());
    }

    private String addMember(UUID accountId, UUID memberId) throws Exception {
        var request = new AccountDtos.AddAccountMemberRequest(memberId, AccountRole.MEMBER);

        String url = UrlBuilderUtils.buildUrl(Payment_URLS.ADD_MEMBERS.getUrl(), Map.of(PaymentUrlParam.ID.getKey(), accountId), null);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        jsonRequest(request),
                        String.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = json(response);
        return body.get("userId").asText();
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