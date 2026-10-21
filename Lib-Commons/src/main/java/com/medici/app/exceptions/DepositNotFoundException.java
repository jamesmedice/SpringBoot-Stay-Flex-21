package com.medici.app.exceptions;


import java.text.MessageFormat;

import static com.medici.app.exceptions.LogExceptionMessages.DEPOSIT_NOT_FOUND;

public class DepositNotFoundException extends RuntimeException {

    public DepositNotFoundException(String accountId) {
        super(MessageFormat.format(DEPOSIT_NOT_FOUND, accountId));
    }
}