package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.helper.DatabaseInitializer;
import org.example.views.LoginApp;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initialize();
        LoginApp loginApp = new LoginApp();
        loginApp.setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}