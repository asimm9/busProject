package org.example.views;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.managers.UserManager;

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
            // Ana panele geçiş burada yapılabilir
        } else {
            statusLabel.setText("Hatalı kullanıcı adı veya şifre.");
        }
    }

    @FXML
    private void onRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Yeni UserModel oluşturmalısın
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        // id ve email gibi diğer alanları da ayarlaman gerekir, örneğin id oluştur
        user.setId(java.util.UUID.randomUUID().toString());

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
