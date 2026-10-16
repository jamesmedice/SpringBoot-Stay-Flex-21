package com.medici.app.service;

import com.medici.app.model.TokenChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.medici.app.constants.ApplicationConstants.TOKEN;

@Slf4j
@Service
public class TokenChainManagerService {

    @Value("${token.expire.timing:60}")
    private String expiryTiming;

    private final AtomicReference<TokenChain> tokenFutureRef = new AtomicReference<TokenChain>();

    public synchronized String getToken() {
        TokenChain future = tokenFutureRef.get();
        log.info("Thread {} START", Thread.currentThread().getName());

        if (future != null && !future.needsRefresh())
            return future.getToken();

        future = fetchNewToken();
        tokenFutureRef.set(future);

        log.info("Thread {} END", Thread.currentThread().getName());
        return future.getToken();
    }

    private TokenChain fetchNewToken() {
        String token = StringUtils.join(TOKEN, UUID.randomUUID().toString());
        Instant expiry = Instant.now().plusSeconds(120);
        return new TokenChain(token, expiry);
    }
}