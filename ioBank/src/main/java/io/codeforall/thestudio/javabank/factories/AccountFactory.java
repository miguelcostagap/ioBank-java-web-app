package io.codeforall.thestudio.javabank.factories;


import io.codeforall.thestudio.javabank.errors.ErrorMessage;
import io.codeforall.thestudio.javabank.model.account.*;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    public Account createAccount(AccountType accountType) {

        Account newAccount;
        switch (accountType) {
            case CHECKING:
                newAccount = new CheckingAccount();
                break;
            case SAVINGS:
                newAccount = new SavingsAccount();
                break;
            default:
                throw new IllegalArgumentException(ErrorMessage.TRANSACTION_INVALID);

        }

        return newAccount;
    }
}
