package com.medici.app.model;

import java.time.Instant;

public class HashInfo {

    private final String hash;
    private final Instant expiryTime;

    public HashInfo(String hash, Instant expiryTime) {
        this.hash = hash;
        this.expiryTime = expiryTime;
    }

    public boolean needsRefresh() {
        return Instant.now().isAfter(expiryTime.minusSeconds(10));
    }

    public String getHash() {
        return hash;
    }
}
