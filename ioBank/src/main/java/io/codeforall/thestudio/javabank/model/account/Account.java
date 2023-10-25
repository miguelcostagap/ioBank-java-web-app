package io.codeforall.thestudio.javabank.model.account;

import io.codeforall.thestudio.javabank.model.AbstractModel;
import io.codeforall.thestudio.javabank.model.Customer;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
@Table(name = "account")
public abstract class Account extends AbstractModel {

    private double balance = 0;

    @ManyToOne
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public abstract AccountType getAccountType();

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
