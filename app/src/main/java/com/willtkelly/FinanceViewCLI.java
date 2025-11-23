package com.willtkelly;

import java.util.List;

class FinanceViewCLI {

    private TransactionService ts;

    public FinanceViewCLI(TransactionService ts) {
        this.ts = ts; 
    }

    public void header() {
        System.out.println("------------------------------------");
        System.out.println(" Finance Tracker | Balance: $" + ts.calculateTotalBalance());
        System.out.println("------------------------------------");

        List<Account> accounts = ts.getAllAccounts();
        for (Account account : accounts) {
            System.out.println(" " +account.getName() + ": $" + account.getBalance());
        }

        System.out.println("");
        System.out.println("------------------------------------");
        System.out.flush();
    }


    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
