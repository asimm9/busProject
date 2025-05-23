package org.example;

import javafx.application.Application;
import org.example.helper.DatabaseInitializer;
import org.example.views.LoginApp;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        Application.launch(LoginApp.class, args);
    }
}