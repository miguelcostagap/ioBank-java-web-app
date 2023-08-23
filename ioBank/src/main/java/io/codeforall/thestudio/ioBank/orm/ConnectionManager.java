package io.codeforall.thestudio.ioBank.orm;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {
    private static final String DEFAULT_USER = "miguel";
    private static final String DEFAULT_PASS = "";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_DB = "iobank";
    private static final String CONNECTOR = "jdbc:postgresql:";

    private String dbUrl;
    private String user;
    private String pass;
    private Connection connection;

    public ConnectionManager(String user, String pass, String host, String database) {
        this.user = user;
        this.pass = pass;
        this.dbUrl = CONNECTOR + "/" + host + "/" + database;
    }

    public ConnectionManager() {
        this(DEFAULT_USER, DEFAULT_PASS, DEFAULT_HOST, DEFAULT_DB);
    }

    @PostConstruct
    public void initialize() {
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbUrl, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failure to create connection to the database: " + e.getStackTrace());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            System.out.println("Didn't found any connection to a database: " + e.getStackTrace());
        }
        return connection;
    }

    public void close() {
        try {
            if (connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close the connection to the database: " + e.getStackTrace());
        }
    }

}
