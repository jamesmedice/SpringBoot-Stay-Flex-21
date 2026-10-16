package com.medici.app.exceptions;


import java.text.MessageFormat;

import static com.medici.app.exceptions.LogExceptionMessages.ACCOUNT_NOT_FOUND;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountId) {
        super(MessageFormat.format(ACCOUNT_NOT_FOUND, accountId));
    }
}