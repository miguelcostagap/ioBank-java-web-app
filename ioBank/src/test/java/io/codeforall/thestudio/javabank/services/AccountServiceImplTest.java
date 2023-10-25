package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.model.account.CheckingAccount;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaTransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountDao accountDao;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private JpaTransactionManager tx;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAccount() {
        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);
        when(accountDao.findById(1)).thenReturn(account);

        // Call the get() method
        Account result = accountService.get(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify that the accountDao.findById() method was called
        verify(accountDao, times(1)).findById(1);
    }

    @Test
    void testDeposit() throws CustomerNotFoundException, io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);
        account.setCustomer(customer);
        customer.addAccount(account);

        when(customerDao.findById(1)).thenReturn(customer);
        when(accountDao.findById(1)).thenReturn(account);

        // Call the deposit() method
        accountService.deposit(1, 1, 100.0);

        // Verify that the account's balance is updated
        assertEquals(100.0, account.getBalance());

        // Verify that the customerDao.saveOrUpdate() method was called
        verify(customerDao, times(1)).saveOrUpdate(customer);
    }

    @Test
    void testWithdraw() throws  CustomerNotFoundException, io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);
        account.setCustomer(customer);
        customer.addAccount(account);

        account.credit(200.0); // Set initial balance

        when(customerDao.findById(1)).thenReturn(customer);
        when(accountDao.findById(1)).thenReturn(account);

        // Call the withdraw() method
        accountService.withdraw(1, 1, 100.0);

        // Verify that the account's balance is updated
        assertEquals(100.0, account.getBalance());

        // Verify that the customerDao.saveOrUpdate() method was called
        verify(customerDao, times(1)).saveOrUpdate(customer);
    }
}
