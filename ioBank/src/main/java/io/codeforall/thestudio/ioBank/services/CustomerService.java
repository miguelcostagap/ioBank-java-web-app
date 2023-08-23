package io.codeforall.thestudio.ioBank.services;

import io.codeforall.thestudio.ioBank.model.Customer;
import io.codeforall.thestudio.ioBank.model.account.Account;

import java.util.List;

public interface CustomerService {
    Customer get(int id);

    List<Customer> list();

    double getBalance(int customerId);

    void add(Customer customer);
    void updateCustomer(Integer id, Customer customer);
    void deleteCustomer(Integer id);
}

