package com.medici.app.exceptions;


import static com.medici.app.exceptions.LogExceptionMessages.INSUFFICIENT_AVAILABLE_FUNDS;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super(INSUFFICIENT_AVAILABLE_FUNDS);
    }
}