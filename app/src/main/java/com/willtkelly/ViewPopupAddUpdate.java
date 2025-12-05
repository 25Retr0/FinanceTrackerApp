package com.willtkelly;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewPopupAddUpdate { 

    private ViewPopupAddUpdateController controller;
    private List<String> accountNames;

    public ViewPopupAddUpdate(List<String> accountNames) {
        this.accountNames = accountNames;
    }

    public void display() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Item");
        popupStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddUpdatePopup.fxml"));
        
        this.controller = new ViewPopupAddUpdateController();
        this.controller.setStage(popupStage);
        this.controller.setAccountNames(accountNames);
        loader.setController(controller);

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Transaction getSubmittedTransaction() {
        if (this.controller.didSubmit()) {
            return this.controller.getTransaction();
        }
        return null;
    }

    public String getSubmittedAccountName() {
        if (this.controller.didSubmit()) {
            return this.controller.getAccountName();
        }
        return null;
    }

}
