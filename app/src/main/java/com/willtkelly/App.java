package com.willtkelly;

import javafx.stage.Stage;

import java.util.ArrayList;

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

        // Setup Model/Service Envirnoment
        DataManager.initialiseDatabase();
        ArrayList<Account> accounts = DataManager.loadAllAccounts();
        TransactionService ts = new TransactionService(accounts);

        // Setup FXML Loader and apply the factory
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view1.fxml"));

        // Set Controller Factory
        loader.setControllerFactory(param -> {
            if (param.equals(FinanceViewUIController.class)) {
                return new FinanceViewUIController(ts);
            }

            try {
                java.lang.reflect.Constructor<?> constructor = param.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
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
