package com.willtkelly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataManager {
    private static final String DB_URL = "jdbc:sqlite:src/data/data.db";

    /**
     *
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initialiseDatabase() {
        String createAccountTable = """
        CREATE TABLE IF NOT EXISTS accounts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            balance DECIMAL,
            name VARCHAR(50)
        );
        """;

        String createTransTable = """
        CREATE TABLE IF NOT EXISTS transactions (
            id INT AUTO_INCREMENT PRIMARY KEY,
            amount DECIMAL,
            category VARCHAR(20),
            date DATE,
            description VARCHAR(255),
            account_id INT,
            FOREIGN KEY(account_id) REFERENCES accounts(id)
        );
        """;

        try (Connection conn = connect()) {
                
            // 1. Create the accounts table
            System.out.println("Attempting to create 'accounts' table...");
            try (PreparedStatement pstmtAccount = conn.prepareStatement(createAccountTable)) {
                pstmtAccount.execute();
                System.out.println("'accounts' table created or already exists.");
            }
            
            // 2. Create the transactions table
            // This MUST happen *after* the accounts table exists due to the FOREIGN KEY
            System.out.println("Attempting to create 'transactions' table...");
            try (PreparedStatement pstmtTrans = conn.prepareStatement(createTransTable)) {
                pstmtTrans.execute();
                System.out.println("'transactions' table created or already exists.");
            }

        } catch (SQLException e) {
            // Handle any error that occurs during connection or execution
            System.err.println("Database initialization failed:");
            System.err.println(e.getMessage());
        }
    }

    public static Account loadAccountWithTransactions(String accountName) {
        String sql = """
            SELECT t.id, t.amount, t.category, t.date, t.description 
            FROM transactions t
            INNER JOIN accounts a ON t.account_id = a.id
            WHERE a.name = ?;
        """;

        Account account = new Account(accountName);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountName);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAmount(rs.getDouble("amount"));
                t.setCategory(Category.valueOf(rs.getString("category")));
                t.setDescription(rs.getString("description"));
                t.setDate(rs.getString("date"));

                account.addTransaction(t);
            }

        } catch (SQLException e) {
            System.err.println("Database query failed:");
            System.err.println(e.getMessage());
            return null;
        }

        return account;
    }


    public static ArrayList<Account> loadAllAccounts() {

        String sqlQuery = "SELECT name FROM accounts;";
        ArrayList<String> accountNames = new ArrayList<>();

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accountNames.add(rs.getString("name")); 
            }

        } catch (SQLException e) {
            System.err.println("Database query failed:");
            System.err.println(e.getMessage());
            return null;
        }

        // Use accountNames and load accounts
        ArrayList<Account> accounts = new ArrayList<>();

        for (String name : accountNames) {
            Account account = loadAccountWithTransactions(name);
            accounts.add(account);
        }

        return accounts;
    }
}
