package com.willtkelly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TransactionService {

    private HashMap<String, Account> accounts;

    public TransactionService() {
        this.accounts = new HashMap<>();
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
        return new ArrayList<>(this.accounts.values());
    }

    /**
     * Adds a new transaction to the specified account.
     *
     * @param accountName - 
     * @param transaction -
     * @return boolean - 
     */
    public boolean addTransaction(String accountName, Transaction transaction) {
        Account account = getAccount(accountName);
        if (account == null) {
            return false;
        }

        account.addTransaction(transaction);
        return true;
    }

    public double getAccountBalance(String accountName) {
        Account account = getAccount(accountName);
        if (account == null) {
            return 0.0;
        }

        return account.getBalance();
    }

    public double calculateTotalBalance() {
        double total = 0;

        for (Account account : this.accounts.values()) {
            total += account.getBalance();
        }

        return total;
    }

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
