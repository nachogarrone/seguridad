package com.ucu.seguridad.controllers;

import com.ucu.seguridad.views.MessagesView;
import com.ucu.seguridad.views.SignView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nachogarrone on 10/29/16.
 */
@Component
public class MainController {
    public MenuItem menu_sign;
    public MenuItem menu_mensajes;
    public Pane container;

    @Autowired
    MessagesView messagesView;

    @Autowired
    SignView signView;

    @FXML
    public void initialize() {
        menu_mensajes.setOnAction((ActionEvent event) -> {

            Stage stage = new Stage();
            if (messagesView.getView() != null && messagesView.getView().getScene() != null) {
                stage.setScene(messagesView.getView().getScene());
            } else {
                stage.setScene(new Scene(messagesView.getView()));
            }
            stage.setTitle("Messages");
            stage.setResizable(true);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.show();

//            container.getScene().getWindow().hide();
        });
        menu_sign.setOnAction(event -> {
            Stage stage = new Stage();
            if (signView.getView() != null && signView.getView().getScene() != null) {
                stage.setScene(signView.getView().getScene());
            } else {
                stage.setScene(new Scene(signView.getView()));
            }
            stage.setTitle("Sign Documents");
            stage.setResizable(true);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.show();

//            container.getScene().getWindow().hide();
        });
    }
}
