package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.domain.Transfer;
import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;

public interface TransferService {

    void transfer(Transfer transfer)
            throws AccountNotFoundException, TransactionInvalidException;

    void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
