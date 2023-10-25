package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("dev")
public class AccountServiceImpl implements AccountService {

    private JpaTransactionManager tx;
    private AccountDao accountDao;
    private CustomerDao customerDao;

    @Override
    public Account get(Integer id) {
        try {

            tx.beginRead();

            return accountDao.findById(id);

        } finally {
            tx.commit();
        }
    }


    @Override
    public void deposit(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException {

        try {
            tx.beginWrite();

            Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                    .orElseThrow(CustomerNotFoundException::new);

            Account account = Optional.ofNullable(accountDao.findById(id))
                    .orElseThrow(AccountNotFoundException::new);

            if (!account.getCustomer().getId().equals(customerId)) {
                throw new AccountNotFoundException();
            }
            if (!account.canCredit(amount)) {
                throw new TransactionInvalidException();
            }
            for (Account a : customer.getAccounts()) {
                if (a.getId().equals(id)) {
                    a.credit(amount);
                }
            }

            customerDao.saveOrUpdate(customer);

            tx.commit();

        } catch (TransactionInvalidException ex) {

            tx.rollback();

        }


    }

    @Override
    public void withdraw(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException {

        tx.beginWrite();

        try {
            Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                    .orElseThrow(CustomerNotFoundException::new);

            Account account = Optional.ofNullable(accountDao.findById(id))
                    .orElseThrow(AccountNotFoundException::new);
            if (!account.canWithdraw()) {
                throw new TransactionInvalidException();
            }
            if (!account.getCustomer().getId().equals(customerId)) {
                throw new AccountNotFoundException();
            }
            if (!account.canDebit(amount)) {
                throw new TransactionInvalidException();
            }
            for (Account a : customer.getAccounts()) {
                if (a.getId().equals(id)) {
                    a.debit(amount);
                }
            }

            customerDao.saveOrUpdate(customer);

            tx.commit();

        } catch (TransactionInvalidException ex) {
            tx.rollback();
            throw new TransactionInvalidException();
        }
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Autowired
    public void setTx(JpaTransactionManager tx) {
        this.tx = tx;
    }


}