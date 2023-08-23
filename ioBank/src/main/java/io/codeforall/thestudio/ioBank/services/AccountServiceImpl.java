package io.codeforall.thestudio.ioBank.services;

import io.codeforall.thestudio.ioBank.model.Customer;
import io.codeforall.thestudio.ioBank.model.account.Account;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service

public class AccountServiceImpl implements AccountService {


    private Map<Integer, Customer> customers;
    private int customerCounter = 0;

    @Override
    public void add(Account account) {
    }

    @Override
    public void deposit(int id, double amount) {
    }

    @Override
    public void withdraw(int id, double amount) {
    }

    @Override
    public Account get(int id) {
        return null;
    }
}
