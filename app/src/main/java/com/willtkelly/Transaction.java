package com.willtkelly;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private LocalDate date;
    private Category category;
    private String description;

    public Transaction(double amount, Category category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = LocalDate.now();
    }

    public double getAmount() { return this.amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return this.date; }

    public void setDate(int day, int month, int year) { 
        LocalDate newDate = LocalDate.of(year, month, day);
        this.date = newDate;
    }

    public Category getCategory() { return this.category; }

    public void setCategory(Category category) { this.category = category; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

}
