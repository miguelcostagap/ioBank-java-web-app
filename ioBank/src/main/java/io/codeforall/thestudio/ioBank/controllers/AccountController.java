package io.codeforall.thestudio.ioBank.controllers;

import io.codeforall.thestudio.ioBank.converters.AccountConverter;
import io.codeforall.thestudio.ioBank.factories.AccountFactory;
import io.codeforall.thestudio.ioBank.services.AccountService;
import io.codeforall.thestudio.ioBank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {
    private AccountService accountService;
    private AccountFactory accountFactory;
    private AccountConverter accountConverter;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
