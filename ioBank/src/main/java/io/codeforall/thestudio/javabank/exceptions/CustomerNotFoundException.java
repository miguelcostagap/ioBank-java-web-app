package io.codeforall.thestudio.javabank.exceptions;

import io.codeforall.thestudio.javabank.errors.ErrorMessage;

public class CustomerNotFoundException extends JavaBankException {

    public CustomerNotFoundException() {
        super(ErrorMessage.CUSTOMER_NOT_FOUND);
    }
}
