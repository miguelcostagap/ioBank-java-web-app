package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.domain.Transfer;
import io.codeforall.thestudio.javabank.exceptions.*;
import io.codeforall.thestudio.javabank.model.AbstractModel;
import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferServiceImp implements TransferService {

    private TransactionManager tx;
    private CustomerDao customerDao;
    private AccountDao accountDao;

    @Override
    public void transfer(Transfer transfer) throws AccountNotFoundException, TransactionInvalidException {

        try {
            tx.beginWrite();

            Account srcAccount = accountDao.findById(transfer.getSrcId());
            Account dstAccount = accountDao.findById(transfer.getDstId());

            accountTransfer(srcAccount, dstAccount, transfer.getAmount());
        } catch (JavaBankException ex) {
            tx.rollback();
        }
    }

    @Override
    public void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        try {
            tx.beginWrite();

            Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                    .orElseThrow(CustomerNotFoundException::new);

            Account srcAccount = Optional.ofNullable(accountDao.findById(transfer.getSrcId()))
                    .orElseThrow(AccountNotFoundException::new);
            Account dstAccount = Optional.ofNullable(accountDao.findById(transfer.getDstId()))
                    .orElseThrow(AccountNotFoundException::new);

            if (!customer.getAccounts().contains(srcAccount)) {
                throw new AccountNotFoundException();
            }
            if (srcAccount.getId() == dstAccount.getId()) {
                throw new AssociationExistsException();
            }

            verifyRecipientId(customer, dstAccount);
            double srcAccountInitialBalance = srcAccount.getBalance();
            accountTransfer(srcAccount, dstAccount, transfer.getAmount());
            if (srcAccount.getBalance() == srcAccountInitialBalance) {
                throw new TransactionInvalidException();
            }
            tx.commit();

        } catch (JavaBankException ex) {
            tx.rollback();
            switch (ex.getClass().getSimpleName()) {
                case "AccountNotFoundException":
                    throw new AccountNotFoundException();
                case "AssociationExistsException":
                    throw new AssociationExistsException();
                case "TransactionInvalidException":
                    throw new TransactionInvalidException();

            }
        }
    }

    private void accountTransfer(Account srcAccount, Account dstAccount, Double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        verifyTransferAccountInformation(srcAccount, dstAccount, amount);

        srcAccount.debit(amount);
        dstAccount.credit(amount);

        accountDao.saveOrUpdate(srcAccount);
        accountDao.saveOrUpdate(dstAccount);
    }

    private void verifyTransferAccountInformation(Account srcAccount, Account dstAccount, double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        Optional.ofNullable(srcAccount)
                .orElseThrow(AccountNotFoundException::new);

        Optional.ofNullable(dstAccount)
                .orElseThrow(AccountNotFoundException::new);

        if (!srcAccount.canDebit(amount) || !dstAccount.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

    }

    private void verifyRecipientId(Customer customer, Account dstAccount) throws AccountNotFoundException {

        List<Integer> recipientAccountIds = listRecipientAccountIds(customer);

        if (!customer.getAccounts().contains(dstAccount) &&
                !recipientAccountIds.contains(dstAccount.getId())) {
            throw new AccountNotFoundException();
        }
    }

    private List<Integer> listRecipientAccountIds(Customer customer) {

        return customer.getRecipients().stream()
                .map(Recipient::getAccountNumber)
                .collect(Collectors.toList());
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

}
