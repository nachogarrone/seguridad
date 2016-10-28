package com.ucu.seguridad.controllers;

import com.ucu.seguridad.models.MessagesEntity;
import com.ucu.seguridad.security.MessageBuilder;
import com.ucu.seguridad.security.MessageHash;
import com.ucu.seguridad.services.MessagesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class MessagesController {

    public TextField messageField;
    public TextField authorField;
    public TextField keyField;
    @FXML
    TableView<MessagesEntity> tableMessages;

    @FXML
    TableColumn<MessagesEntity, String> authorColumn;
    @FXML
    TableColumn<MessagesEntity, String> messageColumn;
    @FXML
    TableColumn decryptColumn;

    @Autowired
    MessagesService messagesService;

    @FXML
    public void initialize() {
        configureUsersTable();
        loadTable();
    }

    private void loadTable() {
        for (MessagesEntity messagesEntity : messagesService.findAllMessages()) {
            try {
                String salt = messagesEntity.getClave().split(":")[0];
                String password = messagesEntity.getClave().split(":")[1];
                messagesEntity.setMessage(MessageHash.decrypt(messagesEntity.getMessage(), password, salt));
            } catch (Exception e) {
                messagesEntity.setMessage("Encrypted message.");
            }
            tableMessages.getItems().add(messagesEntity);
        }
    }

    private void configureUsersTable() {
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
//        decryptColumn.setCellValueFactory(new Bu<>("message"));
    }


    public void addMessage(ActionEvent actionEvent) {
        if (StringUtils.isEmpty(messageField.getText()) || StringUtils.isEmpty(keyField.getText())) {
            // error
            return;
        }
        MessagesEntity message = new MessagesEntity();
        message.setAuthor(authorField.getText());
        MessageBuilder messageBuilder = MessageHash.encrypt(messageField.getText(), keyField.getText());
        message.setMessage(messageBuilder.getMessage());
        message.setClave(messageBuilder.getKey());
        messagesService.save(message);

        loadTable();
    }
}
