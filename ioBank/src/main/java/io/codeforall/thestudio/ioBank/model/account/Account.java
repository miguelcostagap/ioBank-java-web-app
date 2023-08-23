package io.codeforall.thestudio.ioBank.model.account;

public interface Account {
    /**
     * Getters
     */

    Integer getId();

    double getBalance();

    AccountType getAccountType();

    /**
     * Account Movements
     */

    void credit(double amount);

    void debit(double amount);

    /**
     * Validations
     */

    boolean canCredit(double amount);

    boolean canDebit(double amount);

    boolean canWithdraw();

}
