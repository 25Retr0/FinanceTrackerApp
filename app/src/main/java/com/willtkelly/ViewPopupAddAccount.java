package com.willtkelly;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewPopupAddAccount {

    private ViewPopupAddAccountController controller;

    public void display() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Item");
        popupStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccountAddUpdatePopup.fxml"));
        
        this.controller = new ViewPopupAddAccountController();
        this.controller.setStage(popupStage);
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

    public Account getSubmittedAccount() {
        if (this.controller.didSubmit()) {
            return this.controller.getAccount();
        }
        
        return null;
    }
}
