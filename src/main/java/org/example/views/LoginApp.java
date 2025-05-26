package org.example.views;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.UserModel;
import org.example.managers.UserManager;

import java.util.UUID;

public class LoginApp extends Application {

    private VBox formBox;
    private Scene scene;
    private Stage stage;

    private final UserManager userManager = UserManager.getInstance();

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Stop[] stops = {
                new Stop(0, Color.web("#3A6186")),
                new Stop(1, Color.web("#89253E"))
        };
        LinearGradient background = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        root.setBackground(new Background(new BackgroundFill(background, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView busImage = new ImageView(new Image(getClass().getResource("/images/bus.png").toExternalForm()));
        busImage.setFitWidth(100);
        busImage.setFitHeight(60);
        busImage.setPreserveRatio(true);

        StackPane animatedBusPane = new StackPane(busImage);
        animatedBusPane.setMinHeight(80);
        animatedBusPane.setMaxHeight(80);
        animatedBusPane.setAlignment(Pos.CENTER_LEFT);

        VBox container = new VBox(20, animatedBusPane, formBox);
        container.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(container);

        showLoginForm();

        scene = new Scene(root, 500, 420);
        stage.setTitle("Giriş Ekranı");
        stage.setScene(scene);
        stage.show();

        final double sceneWidth = 500;
        final double busWidth = 100;
        final double rightStart = sceneWidth + busWidth;
        final double leftEnd = -busWidth;

        busImage.setTranslateX(rightStart);
        busImage.setScaleX(1);

        TranslateTransition busMove = new TranslateTransition(Duration.seconds(4), busImage);
        busMove.setFromX(rightStart);
        busMove.setToX(leftEnd);
        busMove.setCycleCount(1);

        busMove.setOnFinished(event -> {
            if (busImage.getScaleX() == 1) {
                busImage.setScaleX(-1);
                busImage.setTranslateX(leftEnd);
                busMove.setFromX(leftEnd);
                busMove.setToX(rightStart);
            } else {
                busImage.setScaleX(1);
                busImage.setTranslateX(rightStart);
                busMove.setFromX(rightStart);
                busMove.setToX(leftEnd);
            }
            busMove.playFromStart();
        });

        busMove.play();

        Timeline smokeTimeline = new Timeline(
                new KeyFrame(Duration.millis(300), event -> {
                    Circle smoke = new Circle(5, Color.rgb(200, 200, 200, 0.4));
                    animatedBusPane.getChildren().add(smoke);
                    smoke.setTranslateX(busImage.getTranslateX() - 20);
                    smoke.setTranslateY(10);

                    TranslateTransition moveUp = new TranslateTransition(Duration.seconds(2), smoke);
                    moveUp.setByY(-30);
                    FadeTransition fade = new FadeTransition(Duration.seconds(2), smoke);
                    fade.setToValue(0);
                    ParallelTransition pt = new ParallelTransition(moveUp, fade);
                    pt.setOnFinished(e -> animatedBusPane.getChildren().remove(smoke));
                    pt.play();
                })
        );
        smokeTimeline.setCycleCount(Animation.INDEFINITE);
        smokeTimeline.play();
    }

    private void showLoginForm() {
        formBox.getChildren().clear();

        TextField usernameField = new TextField();
        styleInputField(usernameField);
        usernameField.setPromptText("Kullanıcı Adı");

        PasswordField passwordField = new PasswordField();
        styleInputField(passwordField);
        passwordField.setPromptText("Şifre");

        Button loginButton = new Button("Giriş Yap");
        styleButton(loginButton);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            UserModel user = userManager.login(username, password);
            if (user != null) {
                stage.close();
                if (user.isAdmin()) {
                    new AdminPanel(user);
                } else {
                    new UserDashboard(user);
                }
            } else {
                showAlert("Kullanıcı adı veya şifre yanlış!");
            }
        });

        Label switchToRegister = new Label("Hesabın yoksa ");
        switchToRegister.setTextFill(Color.WHITE);
        switchToRegister.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

        Hyperlink registerLink = new Hyperlink("Kayıt Ol");
        registerLink.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        registerLink.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        registerLink.setOnAction(e -> showRegisterForm());

        HBox linkBox = new HBox(switchToRegister, registerLink);
        linkBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(usernameField, passwordField, loginButton, linkBox);
    }

    private void showRegisterForm() {
        formBox.getChildren().clear();

        TextField emailField = new TextField();
        styleInputField(emailField);
        emailField.setPromptText("E-posta");

        TextField usernameField = new TextField();
        styleInputField(usernameField);
        usernameField.setPromptText("Kullanıcı Adı");

        PasswordField passwordField = new PasswordField();
        styleInputField(passwordField);
        passwordField.setPromptText("Şifre");

        Button registerButton = new Button("Kayıt Ol");
        styleButton(registerButton);

        registerButton.setOnAction(e -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Lütfen tüm alanları doldurun.");
                return;
            }

            UserModel newUser = new UserModel();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setAdmin(username.toLowerCase().contains("admin"));
            newUser.setId(UUID.randomUUID().toString());

            boolean success = userManager.registerUser(newUser);
            if (success) {
                stage.close();
                if (newUser.isAdmin()) {
                    new AdminPanel(newUser);
                } else {
                    new UserDashboard(newUser);
                }
            } else {
                showAlert("Bu kullanıcı zaten kayıtlı!");
            }
        });

        Label switchToLogin = new Label("Zaten hesabın var mı? ");
        switchToLogin.setTextFill(Color.WHITE);
        switchToLogin.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

        Hyperlink loginLink = new Hyperlink("Giriş Yap");
        loginLink.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        loginLink.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        loginLink.setOnAction(e -> showLoginForm());

        HBox linkBox = new HBox(switchToLogin, loginLink);
        linkBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(emailField, usernameField, passwordField, registerButton, linkBox);
    }

    private void styleInputField(TextField field) {
        field.setFont(Font.font("Arial", 14));
        field.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 6 10 6 10;");
        field.setMaxWidth(220);
    }

    private void styleButton(Button button) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.web("#89253E"));
        button.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-cursor: hand;");
        button.setPadding(new Insets(8, 20, 8, 20));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
