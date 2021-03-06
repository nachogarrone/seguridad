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
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
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
    private String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[loa-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        labelError.setText("");

        if (!passwordField1.getText().equals(passwordField2.getText())) {
            labelError.setText("Las passwords deben coincidir");
            return;
        }
        String valid = isValidPassword(passwordField1.getText());
        if (valid != null) {
            labelError.setText("La password es muy debil: " + valid);
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

        if (userService.findByUserName(usernameField.getText()) != null) {
            labelError.setText("Username ya existe");
            return;
        }

        if (userService.findByEmail(emailField.getText()) != null) {
            labelError.setText("Mail ya existe");
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

    private String isValidPassword(String password) {
        StringBuilder error = null;
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // length between 8 and 16 characters
                new LengthRule(8, 16),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(new String(password)));
        if (result.isValid()) {
            System.out.println("Password is valid");
            return null;
        } else {
            System.out.println("Invalid password:");
            for (String err : validator.getMessages(result)) {
                if (err != null) {
                    System.out.println(err);
                    error.append(err);
                }
            }
            return error.toString();
        }
    }
}
