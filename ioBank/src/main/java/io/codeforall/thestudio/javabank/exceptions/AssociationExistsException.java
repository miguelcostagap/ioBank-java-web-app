package io.codeforall.thestudio.javabank.exceptions;

import io.codeforall.thestudio.javabank.errors.ErrorMessage;

public class AssociationExistsException extends JavaBankException {

    public AssociationExistsException() {
        super(ErrorMessage.ASSOCIATION_EXISTS);
    }
}
