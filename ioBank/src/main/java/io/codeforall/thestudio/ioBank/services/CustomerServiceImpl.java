package io.codeforall.thestudio.ioBank.services;

import io.codeforall.thestudio.ioBank.model.Customer;
import io.codeforall.thestudio.ioBank.model.account.Account;
import io.codeforall.thestudio.ioBank.orm.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private AccountService accountService;
    private ConnectionManager connectionManager;

    @Autowired
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public List<Customer> list() {
        String query = "SELECT customer.id AS cid, first_name, last_name, phone, email, photo_url, account.id AS aid " +
                "FROM customer LEFT JOIN account customer.id = account.customer.id";

        Map<Integer, Customer> customers = new HashMap<>();
        try {
            PreparedStatement statement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (!customers.containsKey(resultSet.getInt("cid"))) {
                    Customer customer = buildCustomer(resultSet);
                    customers.put(customer.getId(), customer);
                }
                Account account = accountService.get(resultSet.getInt("aid"));
                if (account != null) {
                    customers.get(resultSet.getInt("cid")).addAccount(account);
                }
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve the customer's list: " + e.getStackTrace());
        }
        return new LinkedList<>(customers.values());
    }

    @Override
    public Customer get(int id) {
        List<Customer> customersList = list();
        Customer customer = null;
        for (Customer c : customersList) {
            if (c.getId() == id) {
                customer = c;
                break;
            }
            System.out.println("Customer not found.");
        }
        return customer;
    }


    @Override
    public double getBalance(int customerId) {
        return 0;
    }

    private Customer buildCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getInt("cid"));
        customer.setFirstName(resultSet.getString("first_name"));
        customer.setLastName(resultSet.getString("last_name"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setEmail(resultSet.getString("email"));
        customer.setProfilePictureURL(resultSet.getString("photo_url"));

        return customer;

    }

    @Override
    public void add(Customer customer) {

        String query = "INSERT INSTO customer(first_name, last_name, email, phone, photo_url) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement statement = connectionManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getProfilePictureURL());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                customer.setId(generatedKeys.getInt(1));
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("Could not add the customer to the database: " + e.getStackTrace());
        }

    }

    @Override
    public void updateCustomer(Integer id, Customer customer) {
        if (customers.containsKey(id)) {
            customers.get(id).setFirstName(customer.getFirstName());
            customers.get(id).setLastName(customer.getLastName());
            customers.get(id).setEmail(customer.getEmail());
            customers.get(id).setPhone(customer.getPhone());
        }
    }

    @Override
    public void deleteCustomer(Integer id) {
        if (id != null && get(id) != null) {
            return;
        }

        String query = "DELETE FROM customer WHERE id=?";

        try {
            PreparedStatement statement = connectionManager.getConnection().prepareStatement(query);

            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            System.out.println("could not delete customer from database: " + e.getStackTrace());
        }
    }

}

