package io.codeforall.thestudio.javabank.exceptions;

import io.codeforall.thestudio.javabank.errors.ErrorMessage;

public class TransactionInvalidException extends JavaBankException {

    public TransactionInvalidException() {
        super(ErrorMessage.TRANSACTION_INVALID);
    }
}
