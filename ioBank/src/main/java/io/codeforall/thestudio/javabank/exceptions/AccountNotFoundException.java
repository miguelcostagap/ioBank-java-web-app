package io.codeforall.thestudio.javabank.exceptions;

import io.codeforall.thestudio.javabank.errors.ErrorMessage;

public class AccountNotFoundException extends JavaBankException {

    public AccountNotFoundException() {
        super(ErrorMessage.ACCOUNT_NOT_FOUND);
    }
}
