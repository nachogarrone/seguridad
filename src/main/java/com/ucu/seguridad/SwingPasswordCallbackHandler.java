package com.ucu.seguridad;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Optional;

public class SwingPasswordCallbackHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordCallback) {
                handlePasswordCallback((PasswordCallback) callbacks[i]);
            } else {
                throw new UnsupportedCallbackException(callbacks[i],
                        "Callback not supported " + callbacks[i].getClass().getName());
            }
        }
    }

    private void handlePasswordCallback(PasswordCallback passCb) throws UnsupportedCallbackException {

//		// dialog
//		JPanel panel = new JPanel();
//		panel.setLayout(new GridLayout(2, 1));
//		
//		// label
//		panel.add(new JLabel(passCb.getPrompt()));
//
//		// passwort input
//		final JTextField txtPwd = new JPasswordField(20);
//		panel.add(txtPwd);
//
//		final JOptionPane pane = new JOptionPane(
//				panel,
//				JOptionPane.QUESTION_MESSAGE,
//				JOptionPane.OK_CANCEL_OPTION);
//		
//		JDialog dialog = pane.createDialog(null, "Login / PIN");
//
//		// set focus to password field
//		dialog.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowOpened(WindowEvent e) {
//				txtPwd.requestFocusInWindow();
//			}
//		});
//		
//		// show dialog
//		dialog.setVisible(true);
//		dialog.dispose();

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("PIN:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<Integer> pin = dialog.showAndWait();

        pin.ifPresent(pinn -> passCb.setPassword(pinn.toString().toCharArray()));


//		int retVal = pane.getValue() != null ? ((Integer) pane.getValue()).intValue() : JOptionPane.CANCEL_OPTION;
//		
//		switch (retVal) {
//		case JOptionPane.OK_OPTION:
//			// return password
//			passCb.setPassword(txtPwd.getText().toCharArray());
//			break;
//		default:
//			// canceled by user
//			throw new CancellationException("Password Callback canceled by user");
//		}
    }

}
