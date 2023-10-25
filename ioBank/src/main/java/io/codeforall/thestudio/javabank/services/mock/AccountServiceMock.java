package io.codeforall.thestudio.javabank.services.mock;

import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.factories.AccountFactory;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.model.account.AccountType;
import io.codeforall.thestudio.javabank.services.AccountService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("mock")
public class AccountServiceMock implements AccountService {

    private Map<Integer, Account> accountMap;
    private AccountFactory accountFactory;

    public AccountServiceMock() {
        accountMap = new HashMap<>();
    }

    @Override
    public Account get(Integer id) {
        return accountMap.get(id);
    }

    @Override
    public void deposit(Integer id, Integer customerId, double amount) throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Account account = get(id);

        if (account.canCredit(amount)) {
            account.credit(amount);
        }

    }

    @Override
    public void withdraw(Integer id, Integer customerId, double amount) throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Account account = get(id);

        if (account.canDebit(amount)) {
            account.debit(amount);
        }
    }

    private int getNextId() {
        return accountMap.isEmpty() ? 1 : Collections.max(accountMap.keySet()) + 1;
    }

    @PostConstruct
    public void populateAccounts() {

        Account a1 = accountFactory.createAccount(AccountType.CHECKING);
        a1.credit(500);

        Account a2 = accountFactory.createAccount(AccountType.CHECKING);
        a2.credit(300);

        Account a3 = accountFactory.createAccount(AccountType.CHECKING);
        a3.credit(200);

        Account a4 = accountFactory.createAccount(AccountType.CHECKING);
        a4.credit(1100);

        accountMap.put(getNextId(), a1);
        accountMap.put(getNextId(), a2);
        accountMap.put(getNextId(), a3);
        accountMap.put(getNextId(), a4);
    }

    @Autowired
    public void setAccountFactory(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }


}
