package com.medici.app.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class TokenChainManagerServiceTest {

    @Autowired
    private TokenChainManagerService tokenChainManagerService;

    @Test
    void getToken_shouldHandleConcurrentRequests() throws Exception {

        int threadCount = 3;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<String> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            executor.submit(() -> {
                log.info("Thread [{}] ", threadIndex);
                readyLatch.countDown();
                try {
                    startLatch.await();
                    log.info("Thread [{}] getToken()", threadIndex);
                    String token = tokenChainManagerService.getToken();
                    results.add(token);
                    log.info("Thread [{}] token [{}]", threadIndex, token);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                    log.info("Thread [{}] end", threadIndex);
                }
            });
        }

        readyLatch.await();
        log.info("[readyLatch.await()]");

        startLatch.countDown();
        log.info("[startLatch.countDown()]");

        doneLatch.await();
        log.info("[doneLatch.await()]");

        executor.shutdown();
        log.info("[executor.shutdown()]");

        // All hashes should be the same
        assertEquals(1, new HashSet<>(results).size());
    }
}
