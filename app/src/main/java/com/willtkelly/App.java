package com.willtkelly;

import javafx.stage.Stage;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Setup Model/Service Envirnoment
        DataManager.initialiseDatabase();
        ArrayList<Account> accounts = DataManager.loadAllAccounts();

        TransactionService ts = new TransactionService(accounts);

        // Set sequence numbers for ids
        int pkSequence = DataManager.getPKSequence("transactions");
        if (pkSequence == -1) { return; }
        ts.setTPKSequence(pkSequence + 1);  // +1 to use next available number

        pkSequence = DataManager.getPKSequence("accounts");
        if (pkSequence == -1) { return; }
        ts.setAPKSequence(pkSequence + 1);  // +1 to use next available number

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
