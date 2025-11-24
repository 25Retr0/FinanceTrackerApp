package com.willtkelly;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class App extends Application {

    public static void testMain(String[] args) {
        System.out.print("Initialising Database...");
        DataManager.initialiseDatabase();
        System.out.print("Completed.\n");

        TransactionService ts = new TransactionService();
        Account a1 = new Account("Savings", 100);
        ts.addAccount(a1);

        Account a2 = new Account("Everyday Spending", 200);
        ts.addAccount(a2);

        Transaction t1 = new Transaction(10, Category.SAVINGS, "Loose change");
        ts.addTransaction("Savings", t1);

        FinanceViewCLI ui = new FinanceViewCLI(ts);
        ui.header();
        ui.commands();
    }

    @Override
    public void start(Stage primaryStage) {
        FinanceViewUI view = new FinanceViewUI();

        Scene scene = new Scene(view.getView(), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Finance App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
