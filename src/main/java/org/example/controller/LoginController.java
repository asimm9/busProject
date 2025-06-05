package org.example.controller;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import org.example.views.AdminPanel;
import org.example.views.LoginApp;
import org.example.views.UserDashboard;

import java.util.UUID;

public class LoginController {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;
    private ImageView busImage;
    private LoginApp view;

    private final UserManager userManager = UserManager.getInstance();

    public LoginController(LoginApp view) {
        this.view = view;
    }

    // Giriş işlemi
    public void onLogin() {
        String username = view.getUsernameField().getText();
        String password = view.getPasswordField().getText();

        UserModel user = userManager.login(username, password);
        if (user != null) {
            view.getStage().close();
            if (user.isAdmin()) {
                new AdminPanel(user);
            } else {
                new UserDashboard(user);
            }
        } else {
            view.showAlert("Kullanıcı adı veya şifre yanlış!");
        }
    }

    // Kayıt işlemi
    public void onRegister() {
        String username = view.getRegisterUsernameField().getText();
        String password = view.getRegisterPasswordField().getText();
        String email =  view.getRegisterEmailField().getText();

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(UUID.randomUUID().toString());
        user.setAdmin(username.toLowerCase().contains("admin"));

        boolean success = userManager.registerUser(user);
        if (success) {
            view.getStage().close();
            if (user.isAdmin()) {
                new AdminPanel(user);
            } else {
                new UserDashboard(user);
            }
        } else {
            view.showAlert("Bu kullanıcı zaten kayıtlı!");
        }
    }
}
