package com.ucu.seguridad;

import com.ucu.seguridad.views.LoginView;
import com.ucu.seguridad.views.UsersView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class App extends AbstractJavaFxApplicationSupport {

    @Autowired
    UsersView usersView;

    @Autowired
    LoginView loginView;
    /**
     * Note that this is configured in application.properties
     */
    @Value("${app.ui.title:Example App}")
    private String windowTitle;

    public static void main(String[] args) {
        launchApp(App.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(windowTitle);
        stage.setScene(new Scene(loginView.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

}
