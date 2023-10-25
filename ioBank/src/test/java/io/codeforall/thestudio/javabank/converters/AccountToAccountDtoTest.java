package io.codeforall.thestudio.javabank.converters;

import io.codeforall.thestudio.javabank.dtos.AccountDto;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.model.account.AccountType;
import io.codeforall.thestudio.javabank.model.account.CheckingAccount;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class AccountToAccountDtoTest {

    private AccountToAccountDto accountToAccountDto;

    @Before
    public void setup() {
        accountToAccountDto = new AccountToAccountDto();
    }

    @Test
    public void testConvert() {

        // setup
        int fakeAccountId = 9999;
        double fakeAccountBalance = 1000.00;

        Account fakeAccount = mock(CheckingAccount.class);

        when(fakeAccount.getId()).thenReturn(fakeAccountId);
        when(fakeAccount.getBalance()).thenReturn(fakeAccountBalance);
        when(fakeAccount.getAccountType()).thenReturn(AccountType.CHECKING);

        // exercise
        AccountDto accountDto = accountToAccountDto.convert(fakeAccount);

        // verify
        verify(fakeAccount).getId();
        verify(fakeAccount).getAccountType();
        verify(fakeAccount).getBalance();

        assertTrue(fakeAccountId == accountDto.getId());
        assertEquals(fakeAccount.getAccountType(), accountDto.getAccountType());
        assertEquals(fakeAccountBalance, accountDto.getBalance());
    }
}