package io.codeforall.thestudio.javabank.exceptions;

import io.codeforall.thestudio.javabank.errors.ErrorMessage;

public class RecipientNotFoundException extends JavaBankException {

    public RecipientNotFoundException() {
        super(ErrorMessage.RECIPIENT_NOT_FOUND);
    }

}
