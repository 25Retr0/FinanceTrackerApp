package com.willtkelly;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewPopupAddAccountController {

    private Stage parent;
    private Account account;
    private boolean submit;
    
    @FXML private TextField accNameTextField;
    @FXML private TextField accBalanceTextField;
    @FXML private Label errorMsgLabel;

    @FXML
    public void initialize() {
        errorMsgLabel.setVisible(false);
    }

    public void setStage(Stage stage) {
        this.parent = stage;
    }

    public Account getAccount() {
        return this.account;
    }

    public boolean didSubmit() {
        return this.submit;
    }

    public void onSave() {
        String name = accNameTextField.getText().trim();
        String amountValue = accBalanceTextField.getText().trim();

        double balance;
        try {
            balance = Double.parseDouble(amountValue);
        } catch (NumberFormatException e) {
            System.err.println("Balance is not a number.");
            errorMsgLabel.setText("Balance is not a number");
            errorMsgLabel.setVisible(true);
            return;
        }

        this.account = new Account();
        this.account.setName(name);
        this.account.setBalance(balance);

        this.submit = true;
        this.parent.close();
    }

    public void onCancel() {
        this.parent.close();
    }
}
