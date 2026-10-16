package com.medici.app.model;

import java.time.Instant;

public class TokenChain {

    private final String token;
    private final Instant expiryTime;

    public TokenChain(String token, Instant expiryTime) {
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public boolean needsRefresh() {
        return Instant.now().isAfter(expiryTime.minusSeconds(20));
    }

    public String getToken() {
        return token;
    }
}
