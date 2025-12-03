package com.willtkelly;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewPopupAddUpdate { 

    private ViewPopupAddUpdateController controller;

    public void display() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Item");
        popupStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddUpdatePopup.fxml"));
        
        this.controller = new ViewPopupAddUpdateController();
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

    public Transaction getSubmittedData() {
        if (this.controller.didSubmit()) {
            return this.controller.getTransaction();
        }

        return null;
    }

}
