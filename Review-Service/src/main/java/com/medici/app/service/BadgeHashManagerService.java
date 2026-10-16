package com.medici.app.service;

import com.medici.app.model.HashInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static com.medici.app.constants.ApplicationConstants.BADGE;

@Slf4j
@Service
public class BadgeHashManagerService {

    @Value("${badge.expire.timing:60}")
    private String expiryTiming;

    private final AtomicReference<CompletableFuture<HashInfo>> hashFutureRef = new AtomicReference<>();

    public String getBadgeHash() {
        CompletableFuture<HashInfo> future = hashFutureRef.get();

        if (future == null || future.join().needsRefresh()) {
            CompletableFuture<HashInfo> newFuture = CompletableFuture.supplyAsync(this::fetchNewHash);
            log.info("Thread {} newFuture={}", Thread.currentThread().getName(), newFuture);

            if (!hashFutureRef.compareAndSet(future, newFuture)) {
                newFuture = hashFutureRef.get();
                log.info("Thread {} CAS failed, using existing future={}", Thread.currentThread().getName(), newFuture);
            } else {
                log.info("Thread {} CAS succeeded, new future set={}", Thread.currentThread().getName(), newFuture);
            }

            future = newFuture;
        }

        HashInfo result = future.join();
        log.info("Thread {} returning hash={}", Thread.currentThread().getName(), result.getHash());
        return result.getHash();
    }

    private HashInfo fetchNewHash() {
        String token = String.format(BADGE, UUID.randomUUID().toString());
        Instant expiry = Instant.now().plusSeconds(120);
        return new HashInfo(token, expiry);
    }
}