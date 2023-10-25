package io.codeforall.thestudio.javabank.dtos;

import io.codeforall.thestudio.javabank.model.account.AccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

public class AccountDto {

    private Integer id;

    @NotNull(message = "account type is mandatory")
    private AccountType accountType;

    @NumberFormat
    @Min(value = 100L, message = "Accounts should have at least 100€ to be created.")
    @NotNull(message = "Initial amount is mandatory")
    private double balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
