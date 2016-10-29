package com.ucu.seguridad.controllers;

import com.ucu.seguridad.models.UserEntity;
import com.ucu.seguridad.security.PasswordAdministrator;
import com.ucu.seguridad.services.UserService;
import com.ucu.seguridad.views.LoginView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class RegisterController {
    public TextField usernameField;
    public PasswordField passwordField1;
    public PasswordField passwordField2;
    public Label labelError;
    public TextField emailField;
    public VBox container;
    @Autowired
    LoginView loginView;
    @Autowired
    UserService userService;
    private String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        labelError.setText("");

        if (StringUtils.isEmpty(passwordField1.getText()) || passwordField1.getText().length() < 6) {
            labelError.setText("La password debe tener largo minimo 6");
            return;
        }
        if (!passwordField1.getText().equals(passwordField2.getText())) {
            labelError.setText("Las passwords deben coincidir");
            return;
        }
        if (StringUtils.isEmpty(usernameField.getText()) || usernameField.getText().length() < 4) {
            labelError.setText("Nombre de usuario debe tener largo minimo 4");
            return;
        }
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (!emailPattern.matcher(emailField.getText()).matches()) {
            labelError.setText("Mail inválido");
            return;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(usernameField.getText());
        userEntity.setEmail(emailField.getText());
        userEntity.setPassword(PasswordAdministrator.encode(passwordField1.getText()));
        userService.save(userEntity);

        // Go to login view
        Stage stage = new Stage();
        if (loginView.getView() != null && loginView.getView().getScene() != null) {
            stage.setScene(loginView.getView().getScene());
        } else {
            stage.setScene(new Scene(loginView.getView()));
        }
        stage.setTitle("Login");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        container.getScene().getWindow().hide();
    }
}
