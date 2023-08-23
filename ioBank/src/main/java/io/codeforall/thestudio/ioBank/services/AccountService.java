package io.codeforall.thestudio.ioBank.services;

import io.codeforall.thestudio.ioBank.model.account.Account;

public interface AccountService {
    void add(Account account);

    void deposit(int id, double amount);

    void withdraw(int id, double amount);

    Account get(int id);
}
