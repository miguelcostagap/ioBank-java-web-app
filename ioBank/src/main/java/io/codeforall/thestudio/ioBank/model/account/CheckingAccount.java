package io.codeforall.thestudio.ioBank.model.account;

public class CheckingAccount extends AbstractAccount {

    public CheckingAccount(Integer id, double balance) {
        super(id, balance);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }

}
