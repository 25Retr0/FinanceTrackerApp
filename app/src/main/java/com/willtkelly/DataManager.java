package com.willtkelly;

import java.io.IOException;
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
}
