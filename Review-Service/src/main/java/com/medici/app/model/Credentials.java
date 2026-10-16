package com.medici.app.model;

import java.time.Instant;

public class Credentials {

    private final String meshup;
    private final Instant expiryTime;

    public Credentials(String meshup, Instant expiryTime) {
        this.meshup = meshup;
        this.expiryTime = expiryTime;
    }

    public boolean needsRefresh() {
        return Instant.now().isAfter(expiryTime.minusSeconds(10));
    }

    public String getMeshup() {
        return meshup;
    }
}
