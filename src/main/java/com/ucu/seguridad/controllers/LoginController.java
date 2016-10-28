package com.ucu.seguridad.controllers;

import com.ucu.seguridad.models.UserEntity;
import com.ucu.seguridad.security.PasswordAdministrator;
import com.ucu.seguridad.services.UserService;
import com.ucu.seguridad.views.MessagesView;
import com.ucu.seguridad.views.RegisterView;
import com.ucu.seguridad.views.UsersView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class LoginController {

    private final int MAX_LOGIN_ATTEMPTS = 3;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label labelError;
    @Autowired
    RegisterView registerView;

    @Autowired
    UsersView usersView;

    @Autowired
    MessagesView messagesView;

    @Autowired
    UserService userService;

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(event);
            }
        });
    }

    public void handleLogin(Event actionEvent) {
        UserEntity userEntity = userService.findByUserName(usernameField.getText());
        if (userEntity == null) {
            labelError.setText("Usuario y/o contraseña inválido");
            return;
        }
        if (userEntity.isLocked()) {
            labelError.setText("Cuenta bloqueada. Contacte al Administrador");
            return;
        }
        if (!PasswordAdministrator.matches(passwordField.getText(), userEntity.getPassword())) {
            labelError.setText("Usuario y/o contraseña inválido");
            userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
            if (userEntity.getLoginAttempts() > MAX_LOGIN_ATTEMPTS) {
                userEntity.setLocked(true);
            }
            userService.save(userEntity);
            return;
        }

        userEntity.setLoginAttempts(0);
        userService.save(userEntity);

        Stage stage = new Stage();
        stage.setScene(new Scene(messagesView.getView()));
        stage.setTitle("Messages");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void handleNoAccountAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setScene(new Scene(registerView.getView()));
        stage.setTitle("Register");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

}
