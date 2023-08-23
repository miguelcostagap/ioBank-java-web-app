package io.codeforall.thestudio.ioBank.factories;

import io.codeforall.thestudio.ioBank.model.account.CheckingAccount;
import io.codeforall.thestudio.ioBank.model.account.Account;
import io.codeforall.thestudio.ioBank.model.account.AccountType;
import io.codeforall.thestudio.ioBank.model.account.SavingsAccount;

/**
 * A factory for creating accounts of different types
 */
public class AccountFactory {

    private int nextAccountId = 1;

    /**
     * Gets the next account id
     *
     * @return the next account id
     */
    private int getNextId() {
        return nextAccountId++;
    }

    /**
     * Creates a new {@link Account}
     *
     * @param accountType the account type
     * @return the new account
     */
    public Account createAccount(AccountType accountType, double balance) {

        Account newAccount;
        switch (accountType) {
            case CHECKING:
                newAccount = new CheckingAccount(getNextId(), balance);
                break;
            case SAVINGS:
                newAccount = new SavingsAccount(getNextId(), balance);
                break;
            default:
                newAccount = null;
        }
        return newAccount;
    }
}
