package io.codeforall.thestudio.ioBank.model.account;

/**
 * A savings account domain entity which requires a minimum balance
 * and can only be used for transferring money, not for debiting
 * @see Account
 * @see AccountType#SAVINGS
 */
public class SavingsAccount extends AbstractAccount {

    /**
     * The minimum balance to maintain on the account
     */
    public static final double MIN_BALANCE = 100;

    /**
     * Creates a new {@code SavingsAccount} instance
     *
     * @see AbstractAccount#AbstractAccount(Integer)
     */
    public SavingsAccount(Integer id, double balance) {
        super(id, balance);
    }

    /**
     * @see Account#getAccountType()
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    /**
     * Checks if the account can be debited without going below the minimum balance
     *
     * @see SavingsAccount#MIN_BALANCE
     * @see Account#canDebit(double)
     */
    @Override
    public boolean canDebit(double amount) {
        return super.canDebit(amount) && (getBalance() - amount) >= MIN_BALANCE;
    }

    /**
     * @see Account#canWithdraw()
     */
    @Override
    public boolean canWithdraw() {
        return false;
    }
}
