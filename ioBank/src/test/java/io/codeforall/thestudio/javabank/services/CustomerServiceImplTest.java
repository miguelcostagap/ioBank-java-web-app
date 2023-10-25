package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.model.account.CheckingAccount;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private AccountDao accountDao;

    @Mock
    private TransactionManager tx;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCustomer() {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);
        when(customerDao.findById(1)).thenReturn(customer);

        // Call the get() method
        Customer result = customerService.get(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify that the customerDao.findById() method was called
        verify(customerDao, times(1)).findById(1);
    }

    @Test
    void testListCustomers() {
        // Create a list of sample customers
        List<Customer> customers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1);
        Customer customer2 = new Customer();
        customer2.setId(2);
        customers.add(customer1);
        customers.add(customer2);

        when(customerDao.findAll()).thenReturn(customers);

        // Call the list() method
        List<Customer> result = customerService.list();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        // Verify that the customerDao.findAll() method was called
        verify(customerDao, times(1)).findAll();
    }

    @Test
    void testGetBalance() {

        Account account = new CheckingAccount();
        account.setId(1);
        account.credit(500.0);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);
        customer.addAccount(account);
        when(customerDao.findById(1)).thenReturn(customer);

        // Call the getBalance() method
        double result = customerService.getBalance(1);

        // Verify the result
        assertEquals(500.0, result);

        // Verify that the customerDao.findById() method was called
        verify(customerDao, times(1)).findById(1);
    }

    @Test
    void testAddCustomer() {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        // Call the add() method
        customerService.add(customer);

        // Verify that the customerDao.saveOrUpdate() method was called
        verify(customerDao, times(1)).saveOrUpdate(customer);
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = mock(Customer.class);
        LinkedList<Account> list = mock(LinkedList.class);

        when(customerDao.findById(1)).thenReturn(customer);
        when(customer.getAccounts()).thenReturn(list);
        when(list.size()).thenReturn(0);
        // Call the delete() method
        customerService.delete(1);

        // Verify that the customerDao.delete() method was called
        verify(customerDao, times(1)).delete(1);
    }

    @Test
    void testAddAccount() throws CustomerNotFoundException {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);

        when(customerDao.findById(1)).thenReturn(customer);
        when(accountDao.findById(1)).thenReturn(account);

        // Call the addAccount() method
        Account result = customerService.addAccount(1, account);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify that the account was added to the customer and saved
        assertTrue(customer.getAccounts().contains(account));
        verify(customerDao, times(1)).saveOrUpdate(customer);
    }

    @Test
    void testCloseAccount() throws CustomerNotFoundException, TransactionInvalidException {
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

        // Call the closeAccount() method
        customerService.closeAccount(1, 1);

        // Verify that the account was removed from the customer and saved
        assertFalse(customer.getAccounts().contains(account));
        verify(customerDao, times(1)).saveOrUpdate(customer);
    }
}
