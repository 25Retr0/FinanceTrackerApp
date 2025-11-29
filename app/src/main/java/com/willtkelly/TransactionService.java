package com.willtkelly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.chart.XYChart.Data;


public class TransactionService {

    private HashMap<String, Account> accounts;
    private Account currentAccount;
    private int TPKSequence;
    private int APKSequence;


    public TransactionService() {
        this.accounts = new HashMap<>();
    }

    public TransactionService(HashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public TransactionService(ArrayList<Account> accountsList) {
        this.accounts = new HashMap<>();
        if (accountsList == null) {
            return;
        }

        for (Account account : accountsList) {
            addAccount(account);
        }

        if (accountsList.size() > 0) {
            currentAccount = accountsList.getFirst();
        }
    }

    public int getTPKSequence() {
        return this.TPKSequence;
    }

    public void setTPKSequence(int n) {
        this.TPKSequence = n;
    }

    public int getAPKSequence() {
        return this.APKSequence;
    }

    public void setAPKSequence(int n) {
        this.APKSequence = n;
    }

    public Account getCurrentAccount() {
        return this.currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    /**
     * Adds a new account to the tracker.
     *
     * @param account - Account Object to add.
     * @return boolean - true if account was added, false otherwise
     */
    public boolean addAccount(Account account) {

        // Check account name is not duplicated
        String account_name = account.getName();
        if (accounts.containsKey(account_name)) {
            return false;
        }

        this.accounts.put(account_name, account);
        return true;
    }

    /**
     * Retrieves a specific account by Name
     *
     * @param accountName - String of the account name to get
     * @return Account if exists, otherwise null
     */
    public Account getAccount(String accountName) {
        if (!this.accounts.containsKey(accountName)) {
            // Throw error message?
            return null;
        }

        return this.accounts.get(accountName);
    }

    /**
     * Get a list of all accounts.
     *
     * @return List of accounts
     */
    public List<Account> getAllAccounts() {
        if (this.accounts == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(this.accounts.values());
    }

    /**
     * Adds a new transaction to the specified account.
     *
     * @param accountName - String name of account to add transaction to
     * @param transaction - Transaction to add to the account
     * @return boolean - True if successful, otherwise false
     */
    public boolean addTransaction(String accountName, Transaction transaction) {
        Account account = getAccount(accountName);
        if (account == null) {
            return false;
        }

        transaction.setId(this.TPKSequence++);

        account.addTransaction(transaction);

        // Save transaction to database
        DataManager.addTransaction(transaction, account);
        
        // Update account balance
        DataManager.updateAccountBalance(account);

        return true;
    }

    /**
     * Get the total balance of a specific account.
     *
     * @param accountName - String name of the account to get balance of
     * @return double - total value of the account balance
     */
    public double getAccountBalance(String accountName) {
        Account account = getAccount(accountName);
        if (account == null) {
            return 0.0;
        }

        return account.getBalance();
    }

    /**
     * Get the total balance of all accounts.
     *
     * @return double - total balance of all accounts
     */
    public double calculateTotalBalance() {
        double total = 0;

        for (Account account : this.accounts.values()) {
            total += account.getBalance();
        }

        return total;
    }

    /**
     * Get the total spending of a specific category
     *
     * @param category - Category to get spending of
     * @return double - total value of spending in specific Category
     */
    public double getSpendingByCategory(Category category) {
        double total = 0;

        for (Account account : this.accounts.values()) {
            for (Transaction transaction : account.getTransactions()) {
                if (transaction.getCategory() == category) {
                    total += transaction.getAmount();
                }
            }
        }
        
        return total;
    }

}
