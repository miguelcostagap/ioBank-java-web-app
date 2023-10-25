package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.exceptions.*;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.RecipientDao;
import io.codeforall.thestudio.javabank.persistence.dao.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Profile("dev")
public class CustomerServiceImpl implements CustomerService {

    private TransactionManager tx;
    private CustomerDao customerDao;
    private AccountDao accountDao;
    private RecipientDao recipientDao;

    @Override
    public Customer get(int id) {

        try {
            tx.beginRead();

            return customerDao.findById(id);

        } finally {
            tx.commit();
        }
    }

    public List<Customer> list() {

        return customerDao.findAll();
    }

    @Override
    public double getBalance(int customerId) {

        try {
            tx.beginRead();

            Customer customer = get(customerId);
            return customer.getBalance();

        } finally {
            tx.commit();
        }
    }

    @Override
    public Customer add(Customer customer) {

        try {
            tx.beginWrite();

            customerDao.saveOrUpdate(customer);

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();
        }
        return customer;
    }

    @Override
    public void delete(int id) {

        try {
            tx.beginWrite();

            Customer customer = customerDao.findById(id);

            if (customer == null) {
                throw new CustomerNotFoundException();
            }

            if (customer.getAccounts().size() > 0) {
                throw new AssociationExistsException();
            }

            customerDao.delete(id);

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();

            if (ex instanceof AssociationExistsException) {
                throw new AssociationExistsException();
            } else if (ex instanceof CustomerNotFoundException) {
                throw new CustomerNotFoundException();
            }
        }
    }

    @Override
    public Account addAccount(Integer id, Account account) throws CustomerNotFoundException {

        Account account1 = null;

        try {
            tx.beginWrite();

            Customer customer = Optional.ofNullable(customerDao.findById(id))
                    .orElseThrow(CustomerNotFoundException::new);

            customer.addAccount(account);
            customerDao.saveOrUpdate(customer);
            account1 = customer.getAccounts().get(customer.getAccounts().size() - 1);

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();
        }
        return account1;
    }

    @Override
    public void closeAccount(Integer id, Integer accountId) throws
            CustomerNotFoundException, TransactionInvalidException {

        try {
            tx.beginWrite();

            Customer customer = Optional.ofNullable(customerDao.findById(id))
                    .orElseThrow(CustomerNotFoundException::new);

            Account account = Optional.ofNullable(accountDao.findById(accountId))
                    .orElseThrow(AccountNotFoundException::new);

            if (!account.getCustomer().getId().equals(id)) {
                throw new CustomerNotFoundException();
            }
            if (account.getBalance() != 0) {
                throw new AssociationExistsException();
            }

            customer.removeAccount(account);
            customerDao.saveOrUpdate(customer);

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();

            if (ex instanceof AssociationExistsException) {
                throw new AssociationExistsException();
            }
        }
    }

    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        Customer customer = null;
        try {
            tx.beginWrite();

            customer = Optional.ofNullable(customerDao.findById(id))
                    .orElseThrow(CustomerNotFoundException::new);

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();
        }
        return new ArrayList<>(customer.getRecipients());
    }

    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException {

        Customer customer = null;

        try {
            tx.beginWrite();

            customer = Optional.ofNullable(customerDao.findById(id))
                    .orElseThrow(CustomerNotFoundException::new);

            Account account = accountDao.findById(recipient.getAccountNumber());

            if (account == null ) {
                throw new TransactionInvalidException();
            }
            if (getAccountIds(customer).contains(recipient.getAccountNumber())){
                throw new AccountNotFoundException();
            }

            Customer customerToBeRecipient = account.getCustomer();

            if (recipient.getId() == null) {

                recipient.setEmail(customerToBeRecipient.getEmail());
                recipient.setPhone(customerToBeRecipient.getPhone());

                customer.addRecipient(recipient);
                customerDao.saveOrUpdate(customer);
            } else {
                recipientDao.saveOrUpdate(recipient);
            }

            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();
            if(ex instanceof AccountNotFoundException){
                throw new AccountNotFoundException();
            } else if (ex instanceof TransactionInvalidException) {
                throw new TransactionInvalidException();
            }
        }
        return customer.getRecipients().get(customer.getRecipients().size() - 1);
    }

    @Override
    public void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, RecipientNotFoundException {

        try {
            tx.beginWrite();

            Customer customer = Optional.ofNullable(customerDao.findById(id))
                    .orElseThrow(CustomerNotFoundException::new);

            Recipient recipient = Optional.ofNullable(recipientDao.findById(recipientId))
                    .orElseThrow(RecipientNotFoundException::new);
            if (!customer.getRecipients().contains(recipient)) {
                throw new RecipientNotFoundException();
            }

            customer.removeRecipient(recipient);
            customerDao.saveOrUpdate(customer);

            tx.commit();

        } catch (JavaBankException ex) {

            tx.rollback();

        }

    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toSet());
    }

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Autowired
    public void setTx(TransactionManager tx) {
        this.tx = tx;
    }

    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }
}