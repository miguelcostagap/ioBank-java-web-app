package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.AccountDto;
import io.codeforall.thestudio.javabank.model.account.Account;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class AccountToAccountDto extends AbstractConverter<Account, AccountDto> {

    public AccountDto convert(Account account) {

        AccountDto accountDto = new AccountDto();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedNumber = decimalFormat.format(account.getBalance());

        accountDto.setId(account.getId());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBalance(Double.parseDouble(formattedNumber));

        return accountDto;
    }
}
