package io.codeforall.thestudio.javabank.model;

import io.codeforall.thestudio.javabank.model.account.Account;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends AbstractModel {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photoURL;

    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "customer",
            fetch = FetchType.EAGER
    )
    private List<Account> accounts;

    @OneToMany(
            // propagate changes on customer entity to account entities
            cascade = {CascadeType.ALL},

            // make sure to remove recipients if unlinked from customer
            orphanRemoval = true,

            // use recipient foreign key on recipient table to establish
            // the many-to-one relationship instead of a join table
            mappedBy = "customer",
            fetch = FetchType.EAGER
    )
    private List<Recipient> recipients;

    public Customer() {
        this.accounts = new ArrayList<>();
        this.recipients = new ArrayList<>();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String imageURL) {
        this.photoURL = imageURL;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void addRecipient(Recipient recipient) {
        recipients.add(recipient);
        recipient.setCustomer(this);
    }

    public void removeRecipient(Recipient recipient) {
        recipients.remove(recipient);
        recipient.setCustomer(null);
    }

    public double getBalance() {
        int balance = 0;

        for (Account a : accounts) {
            balance += a.getBalance();
        }

        return balance;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setCustomer(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setCustomer(null);
    }
}



