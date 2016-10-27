package com.ucu.seguridad.controllers;

import com.ucu.seguridad.security.PasswordAdministrator;
import com.ucu.seguridad.views.RegisterView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class LoginController {

    public PasswordField passwordField;
    public TextField usernameField;

    @Autowired
    RegisterView registerView;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        System.out.println("Username: " + usernameField.getText());
        System.out.println("Password: " + passwordField.getCharacters());

        String securePassword = PasswordAdministrator.encode(passwordField.getText());
        System.out.println("Secure Password: " + securePassword);

        System.out.println("Matches: " + PasswordAdministrator.matches(passwordField.getText(), securePassword));

    }

    public void handleNoAccountAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setScene(new Scene(registerView.getView()));
        stage.setTitle("Register");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
