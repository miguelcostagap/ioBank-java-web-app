package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.RecipientNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.model.account.Account;

import java.util.List;


public interface CustomerService {

    Customer get(int customerId);

    List<Customer> list();

    double getBalance(int customerId);

    Customer add(Customer customer);

    void delete(int id);

    Account addAccount(Integer id, Account account)
            throws CustomerNotFoundException, TransactionInvalidException;

    void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, TransactionInvalidException;

    List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException;

    Recipient addRecipient(Integer id, Recipient recipient)
            throws CustomerNotFoundException, AccountNotFoundException;

    void removeRecipient(Integer id, Integer recipientId)
            throws CustomerNotFoundException, AccountNotFoundException, RecipientNotFoundException;

}

