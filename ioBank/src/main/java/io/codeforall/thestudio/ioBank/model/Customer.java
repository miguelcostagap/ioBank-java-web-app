package io.codeforall.thestudio.ioBank.model;

import io.codeforall.thestudio.ioBank.model.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profilePictureURL;
    private Map<Integer, Account> accounts;

    /**
     * Constructor
     */
    public Customer() {
        accounts = new HashMap<>();
    }

    /**
     * Setters & Getters
     */
    public String getProfilePictureURL() {
        return profilePictureURL;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public double getTotalBalance() {
        double balance = 0;
        for (Account a : getAccounts()) {
            balance += a.getBalance();
        }
        return balance;
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
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

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts.values());
    }


}


