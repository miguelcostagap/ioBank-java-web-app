package io.codeforall.thestudio.ioBank.dto;

public class AccountDTO {

    public Integer id;
    public Double balance;

    /**
     *
     * Getters & Setters
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
