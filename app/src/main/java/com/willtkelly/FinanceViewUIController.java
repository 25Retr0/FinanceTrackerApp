package com.willtkelly;

import java.time.LocalDate;
import java.util.ArrayList;

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




        // TODO: FXML: Show accounts as cards on sidebar


        System.out.println("Application ready.");
    }

}
