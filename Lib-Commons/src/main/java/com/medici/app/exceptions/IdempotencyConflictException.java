package com.medici.app.exceptions;

import static com.medici.app.exceptions.LogExceptionMessages.IDEMPOTENCY_KEY_ALREADY_USED_WITH_DIFFERENT_REQUEST_DATA;

public class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException() {
        super(IDEMPOTENCY_KEY_ALREADY_USED_WITH_DIFFERENT_REQUEST_DATA);
    }
}