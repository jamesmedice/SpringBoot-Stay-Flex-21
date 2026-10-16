package com.medici.app.service;

import com.medici.app.model.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.medici.app.constants.ApplicationConstants.TOKEN;

/**
 * Atomically updates the credentials using compareAndSet.
 * Only one concurrent refresh can successfully replace the current value,
 * so synchronization is not required.
 */

@Slf4j
@Service
public class CredentialsManagerService {

    @Value("${credetials.expire.timing:60}")
    private String expiryTiming;

    private final AtomicReference<Credentials> credentialsFutureRef = new AtomicReference<>();

    public String getMeshup() {
        Credentials future = credentialsFutureRef.get();
        log.info("Thread {} START", Thread.currentThread().getName());

        if (future != null && !future.needsRefresh())
            return future.getMeshup();

        Credentials newFuture = fetchNewMeshup();

        if (credentialsFutureRef.compareAndSet(future, newFuture))
            return credentialsFutureRef.get().getMeshup();

        log.info("Thread {} END", Thread.currentThread().getName());
        return credentialsFutureRef.get().getMeshup();
    }

    private Credentials fetchNewMeshup() {
        String token = StringUtils.join(TOKEN, UUID.randomUUID().toString());
        Instant expiry = Instant.now().plusSeconds(120);
        return new Credentials(token, expiry);
    }
}