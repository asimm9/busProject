package org.example.controller;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import org.example.views.AdminPanel;
import org.example.views.UserDashboard;

import java.util.UUID;

public class LoginController {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;
    private ImageView busImage;

    private final UserManager userManager = UserManager.getInstance();

    // Set metotları: LoginApp'ten bileşenleri alır
    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setBusImage(ImageView busImage) {
        this.busImage = busImage;
        Image bus = new Image(getClass().getResourceAsStream("/images/bus.png"));
        this.busImage.setImage(bus);
    }

    // Giriş işlemi
    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserModel user = userManager.login(username, password);
        if (user != null) {
            statusLabel.setText("Giriş başarılı!");

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            if (user.isAdmin()) {
                new AdminPanel(user);
            } else {
                new UserDashboard(user);
            }
        } else {
            statusLabel.setText("Hatalı kullanıcı adı veya şifre.");
        }
    }

    // Kayıt işlemi
    public void onRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = "asimisik9@gmail.com"; // sabit email - istersen dışarıdan al

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(UUID.randomUUID().toString());
        user.setAdmin(username.toLowerCase().contains("admin"));

        boolean success = userManager.registerUser(user);
        if (success) {
            statusLabel.setText("Kayıt başarılı!");
        } else {
            statusLabel.setText("Kullanıcı zaten mevcut.");
        }
    }
}
