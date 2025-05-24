package org.example.controller;
import javafx.stage.Stage;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.views.AdminPanel;
import org.example.views.UserDashboard;

import java.util.UUID;


public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private final UserManager userManager = UserManager.getInstance();

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserModel user = userManager.login(username, password);
        if (user != null) {
            statusLabel.setText("Giriş başarılı!");
            if (user.isAdmin()) {
                new AdminPanel(user);
            }else{
                new UserDashboard(user);

            }
            // Ana panele geçiş burada yapılabilir
        } else {
            statusLabel.setText("Hatalı kullanıcı adı veya şifre.");
        }
    }

    @FXML
    private void onRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = "asimisik9@gmail.com";

        // Yeni UserModel oluşturmalısın
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(UUID.randomUUID().toString());
        boolean result = user.getUsername().contains("admin");
        user.setAdmin(result);

        boolean success = userManager.registerUser(user);
        if (success) {
            statusLabel.setText("Kayıt başarılı!");
        } else {
            statusLabel.setText("Kullanıcı zaten mevcut.");
        }
    }

    @FXML
    private ImageView busImage;

    @FXML
    public void initialize() {
        Image bus = new Image(getClass().getResourceAsStream("/images/bus.png"));
        busImage.setImage(bus);
    }
}
