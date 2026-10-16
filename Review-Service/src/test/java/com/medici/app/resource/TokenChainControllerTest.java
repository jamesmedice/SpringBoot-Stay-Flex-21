package com.medici.app.resource;

import com.medici.app.utils.TokenChain_URLS;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class})
@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenChainControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeEach
    public void init() {
    }

    @Order(1)
    @Test
    public void getToken_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(TokenChain_URLS.CODE.getUrl(), HttpMethod.GET, null, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Order(2)
    @Test
    public void getToken_shouldReturnSameTokenForSequentialCalls_test() {
        ResponseEntity<String> first = testRestTemplate.exchange(TokenChain_URLS.CODE.getUrl(), HttpMethod.GET, null, String.class);

        ResponseEntity<String> second = testRestTemplate.exchange(TokenChain_URLS.CODE.getUrl(), HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, first.getStatusCode());
        assertEquals(HttpStatus.OK, second.getStatusCode());

        assertNotNull(first.getBody());
        assertNotNull(second.getBody());

        assertEquals(first.getBody(), second.getBody());
    }
}
