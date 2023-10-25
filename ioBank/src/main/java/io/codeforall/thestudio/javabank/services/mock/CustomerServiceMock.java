package io.codeforall.thestudio.javabank.services.mock;

import io.codeforall.thestudio.javabank.exceptions.AccountNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.CustomerNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.RecipientNotFoundException;
import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.services.AccountService;
import io.codeforall.thestudio.javabank.services.CustomerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("mock")
public class CustomerServiceMock implements CustomerService {

    private AccountService accountService;
    private Map<Integer, Customer> customerMap;

    @Autowired
    public CustomerServiceMock(AccountService accountService) {
        this.accountService = accountService;
        customerMap = new HashMap<>();
    }

    @Override
    public Customer get(int id) {
        return customerMap.get(id);
    }

    @Override
    public List<Customer> list() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public double getBalance(int id) {
        return get(id).getBalance();
    }

    @Override
    public Customer add(Customer customer) {

        if (customer.getId() == null) {
            customer.setId(getNextId());
        }

        customerMap.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public void delete(int id) {
        customerMap.remove(id);
    }

    @Override
    public Account addAccount(Integer id, Account account) throws CustomerNotFoundException, TransactionInvalidException {


        Customer customer = customerMap.get(id);

        if (customer == null) {
            return null;
        }
        customer.addAccount(account);

        return account;
    }

    @Override
    public void closeAccount(Integer id, Integer accountId) throws CustomerNotFoundException, TransactionInvalidException {

        Customer customer = customerMap.get(id);

        if (customer == null) {
            return;
        }

        customer.setAccounts(customer.getAccounts()
                .stream()
                .filter(account -> account.getId() != accountId).toList());
    }

    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        return Optional.ofNullable(customerMap.get(id))
                .orElseThrow(CustomerNotFoundException::new)
                .getRecipients();
    }

    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = customerMap.get(id);

        if (accountService.get(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            return null;
        }

        if (recipient.getId() == null) {
            recipient.setId(getNextId());
        }

        customer.addRecipient(recipient);

        return recipient;
    }

    @Override
    public void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, AccountNotFoundException, RecipientNotFoundException {

        Customer customer = Optional.ofNullable(customerMap.get(id))
                .orElseThrow(CustomerNotFoundException::new);

        Recipient recipient = null;

        for (Recipient rcpt : customer.getRecipients()) {
            if (rcpt.getId().equals(recipientId)) {
                recipient = rcpt;
            }
        }

        if (recipient == null) {
            throw new RecipientNotFoundException();
        }

        customer.removeRecipient(recipient);
    }

    private int getNextId() {
        return customerMap.isEmpty() ? 1 : Collections.max(customerMap.keySet()) + 1;
    }

    @PostConstruct
    public void populateCustomers() {

        Customer c1 = new Customer();
        c1.setFirstName("João");
        c1.setLastName("Oliveira");
        c1.setEmail("oliveira@thestud.io");
        c1.setPhone("444555666");
        c1.setPhotoURL("user-profile-3.png");

        Customer c2 = new Customer();
        c2.setFirstName("João");
        c2.setLastName("Townsend");
        c2.setEmail("townsend@thestud.io");
        c2.setPhone("111222333");
        c2.setPhotoURL("user-profile-2.png");

        Customer c3 = new Customer();
        c3.setFirstName("Sara");
        c3.setLastName("Lopes");
        c3.setEmail("lopes@thestud.io");
        c3.setPhone("777888999");
        c3.setPhotoURL("user-profile.png");

        add(c1);
        add(c2);
        add(c3);

        addAccount(c1.getId(), accountService.get(1));
        addAccount(c2.getId(), accountService.get(2));
        addAccount(c2.getId(), accountService.get(4));
        addAccount(c3.getId(), accountService.get(3));
    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toSet());
    }
}
