package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.AccountDto;
import io.codeforall.thestudio.javabank.factories.AccountFactory;
import io.codeforall.thestudio.javabank.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class AccountDtoToAccount implements Converter<AccountDto, Account> {

    private AccountFactory accountFactory;

    @Autowired
    public void setAccountFactory(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    @Override
    public Account convert(AccountDto accountDto) {

        Account account = null;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedNumber = decimalFormat.format(accountDto.getBalance());

        account = accountFactory.createAccount(accountDto.getAccountType());
        account.credit(accountDto.getBalance() != 0.0 ? Double.parseDouble(formattedNumber) : 0);

        return account;
    }
}

