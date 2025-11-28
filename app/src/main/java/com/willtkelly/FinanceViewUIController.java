package com.willtkelly;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FinanceViewUIController {

    private TransactionService ts;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Number> idColumn;

    @FXML
    private TableColumn<Transaction, Number> amountColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private void initialize() {
        // Initialize database
        DataManager.initialiseDatabase();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

       
        // Load User Data (Accounts, Transactions...)
        // Init a TransactionService.
        ArrayList<Account> accounts = DataManager.loadAllAccounts();
        this.ts = new TransactionService(accounts);

        // Set table data, default to first account name.

        // Get transactions of first account name
        List<Account> allAccs = ts.getAllAccounts();
        System.out.println(allAccs);
        if (allAccs.size() == 0) {
            System.err.println("No Accounts Available...");
            return;
        }

        Account acc = allAccs.getFirst();
        List<Transaction> trans = acc.getTransactions();
        System.out.println(trans);

        ObservableList<Transaction> shownTransactions = FXCollections.observableArrayList(trans);
        System.out.println(shownTransactions);
        transactionTable.setItems(shownTransactions);


        // TODO: FXML: Show accounts as cards on sidebar

        System.out.println("Application ready.");
    }

}
