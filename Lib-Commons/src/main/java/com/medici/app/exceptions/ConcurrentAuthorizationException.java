package com.medici.app.exceptions;

import static com.medici.app.exceptions.LogExceptionMessages.CONCURRENT_ACCOUNT_UPDATE_RETRY_WITH_THE_SAME_IDEMPOTENCY_KEY;

public class ConcurrentAuthorizationException extends RuntimeException {

    public ConcurrentAuthorizationException() {
        super(CONCURRENT_ACCOUNT_UPDATE_RETRY_WITH_THE_SAME_IDEMPOTENCY_KEY);
    }
}