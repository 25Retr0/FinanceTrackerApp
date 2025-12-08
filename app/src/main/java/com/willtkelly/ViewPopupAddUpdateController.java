package com.willtkelly;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewPopupAddUpdateController {

    private Stage parent;
    private Transaction transaction;
    private String accountName;
    private boolean submit; 
    private List<String> accountNames;

    @FXML private TextField accountTextField;
    @FXML private TextField amountTextField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextArea descriptionTextField;

    @FXML private Label errorMsgLabel;

    @FXML
    public void initialize() {
        initCategoryBox();
        errorMsgLabel.setVisible(false);
    }

    public void setStage(Stage stage) {
        this.parent = stage;
    }

    public void setAccountNames(List<String> accountNames) {
        this.accountNames = accountNames;
    }

    public void initCategoryBox() {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (Category category : Category.values()) {
            options.add(category.name()); 
        }
        this.categoryChoiceBox.setItems(options);
        this.categoryChoiceBox.setValue(options.getFirst());
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public boolean didSubmit() {
        return this.submit;
    }

    public void onSave() {
        // Save the submitted data
        String accountValue = accountTextField.getText().trim();
        String amountValue = amountTextField.getText().trim();
        String categoryValue = categoryChoiceBox.getValue();
        String descriptionValue = descriptionTextField.getText().trim();

        // System.out.println("Account: " + accountValue);
        // System.out.println("Amount: " + amountValue);
        // System.out.println("Category: " + categoryValue);
        // System.out.println("Description: " + descriptionValue);

        // Check submitted data for errors
        // Check account exists
        if (!this.accountNames.contains(accountValue)) {
            // Account does not exists error
            System.err.println("Account does not exist.");
            errorMsgLabel.setText("Account does not exist");
            errorMsgLabel.setVisible(true);
            return;
        }

        // Check amount is a number
        int amount;
        try {
            amount = Integer.parseInt(amountValue);
        } catch (NumberFormatException e) {
            System.err.println("Amount is not a number.");
            errorMsgLabel.setText("Amount is not a number");
            errorMsgLabel.setVisible(true);
            return;
        }
        
        // Add data to the transaction
        this.transaction = new Transaction();
        this.accountName = accountValue;
        this.transaction.setAmount(amount);
        this.transaction.setCategory(Category.valueOf(categoryValue));
        this.transaction.setDescription(descriptionValue);
        this.transaction.setDate(LocalDate.now());

        this.submit = true;
        this.parent.close();
    }

    public void onCancel() {
        this.parent.close();
    }

}
