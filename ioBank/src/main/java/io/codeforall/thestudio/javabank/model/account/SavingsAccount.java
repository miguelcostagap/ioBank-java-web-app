package io.codeforall.thestudio.javabank.model.account;

import jakarta.persistence.Entity;

@Entity
public class SavingsAccount extends Account {

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

}