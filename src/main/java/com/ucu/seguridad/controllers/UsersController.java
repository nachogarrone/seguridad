package com.ucu.seguridad.controllers;

import com.ucu.seguridad.models.UserEntity;
import com.ucu.seguridad.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class UsersController {
    @FXML
    TableView<UserEntity> tableUsers;

    @FXML
    TableColumn<UserEntity, String> userNameColumn;
    @FXML
    TableColumn<UserEntity, String> userEmailColumn;

    @Autowired
    UserService userService;

    @FXML
    public void initialize() {
        configureUsersTable();

        for (UserEntity userEntity : userService.findAllUsers()) {
            tableUsers.getItems().add(userEntity);
        }
    }

    private void configureUsersTable() {
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
}
