package com.ucu.seguridad.controllers;

import com.ucu.seguridad.models.MessagesEntity;
import com.ucu.seguridad.security.MessageBuilder;
import com.ucu.seguridad.security.MessageHash;
import com.ucu.seguridad.services.MessagesService;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Component
public class MessagesController {

    public TextField messageField;
    public TextField authorField;
    public TextField keyField;
    public Label errorMessage;
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
        List<MessagesEntity> messages = messagesService.findAllMessages();
        for (MessagesEntity message : messages) {
            if (!tableMessages.getItems().contains(message)) tableMessages.getItems().add(message);
        }
    }

    private void configureUsersTable() {
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        decryptColumn.setCellFactory(
                new Callback<TableColumn<MessagesEntity, Boolean>, TableCell<MessagesEntity, Boolean>>() {
                    @Override
                    public TableCell<MessagesEntity, Boolean> call(TableColumn<MessagesEntity, Boolean> personBooleanTableColumn) {
                        return new DecryptMessageCell(tableMessages);
                    }
                });
    }

    public void addMessage() {
        errorMessage.setText("");
        if (StringUtils.isEmpty(messageField.getText())) {
            errorMessage.setText("No va a escribir ningún mensaje?");
            return;
        }
        if (StringUtils.isEmpty(keyField.getText())) {
            errorMessage.setText("Debe protegerlo con alguna clave...");
            return;
        }

        MessagesEntity message = new MessagesEntity();
        message.setAuthor(StringUtils.isEmpty(authorField.getText()) ? "Anonymous" : authorField.getText());
        MessageBuilder messageBuilder = MessageHash.encrypt(messageField.getText(), keyField.getText());
        message.setMessage(messageBuilder.getMessage());
        message.setClave(messageBuilder.getKey());
        messagesService.save(message);

        loadTable();
    }

    private String showDecryptDialog(String encryptedText, String salt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password");
        dialog.setHeaderText("Ingrese la password para descifrar el mensaje.");

        Optional<String> result = dialog.showAndWait();
        String entered = "";

        if (result.isPresent()) {
            entered = result.get();
        }

        String decryptedText = "";
        try {
            decryptedText = MessageHash.decrypt(encryptedText, entered, salt);
        } catch (Exception e) {
            decryptedText = "Couldn't decrypt message";
        }

        return decryptedText;
    }

    private class DecryptMessageCell extends TableCell<MessagesEntity, Boolean> {
        // a button for adding a new person.
        final Button addButton = new Button("Decrypt");
        // pads and centers the add button in the cell.
        final StackPane paddedButton = new StackPane();
        // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        DecryptMessageCell(final TableView table) {
            paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(addButton);
            addButton.setOnMousePressed(mouseEvent -> buttonY.set(mouseEvent.getScreenY()));
            addButton.setOnAction(actionEvent -> {
                MessagesEntity record = (MessagesEntity) DecryptMessageCell.this.getTableRow().getItem();
                String result = showDecryptDialog(record.getMessage(), record.getClave());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(result);
                alert.showAndWait();
            });
        }

        /**
         * places an add button in the row only if the row is not empty.
         */
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }
}
