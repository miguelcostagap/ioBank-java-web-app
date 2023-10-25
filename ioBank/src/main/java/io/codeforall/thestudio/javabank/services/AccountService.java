package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.account.Account;

public interface AccountService {

    Account get(Integer id);

    void deposit(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException;


    void withdraw(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException;
}