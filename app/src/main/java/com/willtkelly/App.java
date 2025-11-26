package com.willtkelly;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/view1.fxml"));
            Scene scene = new Scene(root, 700, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Finance Tracker");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
