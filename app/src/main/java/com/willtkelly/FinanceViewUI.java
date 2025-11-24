package com.willtkelly;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Parent;

public class FinanceViewUI {

    public Parent getView() {
        VBox root = new VBox(10);
        root.getChildren().add(new Label("Finance Overview"));
        return root;
    }

}
