package com.medici.app.resource;

import com.medici.app.utils.BadgeHash_URLS;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class})
@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BadgeHashControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeEach
    public void init() {
    }

    @Test
    public void getBadgeHash_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(BadgeHash_URLS.HASH.getUrl(), HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getBadgeHash_shouldReturnSameHashForSequentialCalls_test() {

        ResponseEntity<String> first = testRestTemplate.exchange(BadgeHash_URLS.HASH.getUrl(), HttpMethod.GET, null, String.class);

        ResponseEntity<String> second = testRestTemplate.exchange(BadgeHash_URLS.HASH.getUrl(), HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, first.getStatusCode());
        assertEquals(HttpStatus.OK, second.getStatusCode());

        assertNotNull(first.getBody());
        assertNotNull(second.getBody());

        assertEquals(first.getBody(), second.getBody());
    }

    @Test
    void getBadgeHash_shouldReturnSameHashForConcurrentRequests_test() throws Exception {
        int threadCount = 3;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<String> responses = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i; // capture index for logging
            executor.submit(() -> {
                log.info("Thread {}", threadIndex);
                readyLatch.countDown();
                try {
                    startLatch.await(); // all threads wait here
                    log.info("Thread {} getForEntity()", threadIndex);
                    ResponseEntity<String> response = testRestTemplate.getForEntity(BadgeHash_URLS.HASH.getUrl(), String.class);
                    responses.add(response.getBody());
                    log.info("Thread {} hash={}", threadIndex, response.getBody());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                    log.info("Thread {} done", threadIndex);
                }
            });
        }

        log.info("[readyLatch.await()]");
        readyLatch.await();

        log.info("[startLatch.countDown()]");
        startLatch.countDown();

        log.info("[doneLatch.await()]");
        doneLatch.await();

        log.info("[executor.shutdown()]");
        executor.shutdown();

        assertEquals(1, new HashSet<>(responses).size());
    }

}
