package com.willtkelly;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewPopupAddUpdateController {

    private Stage parent;
    private Transaction transaction;
    private boolean submit; 

    @FXML private TextField accountTextField;
    @FXML private TextField amountTextField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextArea descriptionTextField;

    @FXML
    public void initialize() {
        initCategoryBox();
    }

    public void setStage(Stage stage) {
        this.parent = stage;
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

    public boolean didSubmit() {
        return this.submit;
    }

    public void onSave() {
        // Save the submitted data
        String accountValue = accountTextField.getText();
        String amountValue = amountTextField.getText();
        String categoryValue = categoryChoiceBox.getValue();
        String descriptionValue = descriptionTextField.getText();

        // Check submitted data for errors
        // Check account exists

        // Check amount is a number

        
        // Make didSubmit true;
        this.submit = true;

        this.parent.close();
    }

    public void onCancel() {
        this.parent.close();
    }

}
