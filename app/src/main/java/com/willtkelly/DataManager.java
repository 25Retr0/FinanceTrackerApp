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
            id INT NOT NULL UNIQUE,
            balance DECIMAL,
            name VARCHAR(50),
            PRIMARY KEY (id AUTOINCREMENT)
        );
        """;

        String createTransTable = """
        CREATE TABLE IF NOT EXISTS transactions (
            id INT NOT NULL UNIQUE,
            amount DECIMAL,
            category VARCHAR(20),
            date DATE,
            description VARCHAR(255),
            account_id INT,
            PRIMARY KEY (id AUTOINCREMENT),
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

    public static int getPKSequence(String tableName) {
        String query = """
            SELECT seq FROM sqlite_sequence
            WHERE name = ?;
        """;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int sequence = rs.getInt("seq");
            return sequence;

        } catch (SQLException e) {
            System.err.println("Query of sequence failed:");
            System.err.println(e.getMessage());
            return -1;
        }
    };

    public static Account loadAccountWithTransactions(int accountId, String accountName) {
        String sql = """
            SELECT t.id, t.amount, t.category, t.date, t.description 
            FROM transactions t
            INNER JOIN accounts a ON t.account_id = a.id
            WHERE a.name = ?;
        """;

        Account account = new Account(accountId, accountName);

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

        String sqlQuery = "SELECT id, name FROM accounts;";
        ArrayList<Integer> accountIds = new ArrayList<>();
        ArrayList<String> accountNames = new ArrayList<>();

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accountIds.add(rs.getInt("id"));
                accountNames.add(rs.getString("name")); 
            }

        } catch (SQLException e) {
            System.err.println("Database query failed:");
            System.err.println(e.getMessage());
            return null;
        }

        // Use accountNames and load accounts
        ArrayList<Account> accounts = new ArrayList<>();

        for (int i = 0; i < accountIds.size(); i++) {
            int id = accountIds.get(i);
            System.out.println(id);
            String name = accountNames.get(i);
            System.out.println(name);
            Account account = loadAccountWithTransactions(id, name);
            accounts.add(account);
        }

        return accounts;
    }


    public static boolean addTransaction(Transaction t, Account a) {
        String query = """
            INSERT INTO transactions (amount, category, date, description, account_id)
            VALUES (?, ?, ?, ?, ?);
        """;
        System.out.println("Attempting to add Transaction to Database");

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {

            System.out.print("Adding Transaction: (");
            System.out.print(t.getAmount() + " ");
            System.out.print(t.getCategory() + " ");
            System.out.print(t.getDate() + " ");
            System.out.print(t.getDescription() + " ");
            System.out.print(a.getId());
            System.out.print(")\n");

            pstmt.setDouble(1, t.getAmount());
            pstmt.setString(2, t.getCategory().toString());
            pstmt.setDate(3, java.sql.Date.valueOf(t.getDate()));
            pstmt.setString(4, t.getDescription());
            pstmt.setInt(5, a.getId());

            pstmt.executeUpdate();
            System.out.println("Transaction Successfully Added");

        } catch (SQLException e) {
            System.err.println("Adding transaction to database failed.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean updateAccountBalance(Account account) {
        String updateSql = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?;
        """;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(updateSql)
        ) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setInt(2, account.getId());

            pstmt.executeUpdate();
            System.out.println("Account balance successfully updated");
        } catch (SQLException e) {
            System.err.println("Updating account balance to database failed.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
