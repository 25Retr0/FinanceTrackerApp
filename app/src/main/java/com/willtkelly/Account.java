package com.willtkelly;

import java.util.ArrayList;

public class Account {

    private int id;
    private String name;
    private double balance;
    private ArrayList<Transaction> transactions;

    /**
     *
     */
    public Account(
        int id,
        String name,
        double balance,
        ArrayList<Transaction> transactions
    ) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions;
    }


    public Account(
        int id,
        String name,
        double balance
    ) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    /**
     *
     */
    public Account(int id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public Account() { }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public double getBalance() { return this.balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public ArrayList<Transaction> getTransactions() { 
        return new ArrayList<>(this.transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.addFirst(transaction);
        setBalance(balance + transaction.getAmount());
    }

}
