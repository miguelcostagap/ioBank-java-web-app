package io.codeforall.thestudio.ioBank.model.account;

public abstract class AbstractAccount implements Account {

    /**
     * Properties
     */

    private Integer id;
    private double balance = 0;

    /**
     * Constructor
     */

    public AbstractAccount(Integer id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    /**
     * Getters
     */

    public Integer getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public abstract AccountType getAccountType();

    /**
     * Account Movements
     */
    public void credit(double amount) {
        if (canCredit(amount)) {
            balance += amount;
        }
    }
    public void debit(double amount) {
        if (canDebit(amount)) {
            balance -= amount;
        }
    }

    /**
     * Validations
     */

    public boolean canCredit(double amount) {
        return amount > 0;
    }

    public boolean canDebit(double amount) {
        return amount > 0 && amount <= balance;
    }

    public boolean canWithdraw() {
        return true;
    }
}
