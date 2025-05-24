package org.example.views;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.UserModel;

public class AdminPanel {

    private final UserModel adminUser;

    public AdminPanel(UserModel adminUser) {
        this.adminUser = adminUser;
        show();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Admin Paneli - Hoş geldin, " + adminUser.getUsername());

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titleLabel = new Label("Sefer Ekle");

        TextField originField = new TextField();
        originField.setPromptText("Kalkış Yeri");

        TextField destinationField = new TextField();
        destinationField.setPromptText("Varış Yeri");

        TextField dateTimeField = new TextField();
        dateTimeField.setPromptText("Tarih ve Saat (yyyy-MM-ddTHH:mm)");

        TextField busIdField = new TextField();
        busIdField.setPromptText("Otobüs ID");

        Button addTripButton = new Button("Sefer Ekle");
        Label messageLabel = new Label();

        addTripButton.setOnAction(e -> {
            String origin = originField.getText();
            String destination = destinationField.getText();
            String dateTime = dateTimeField.getText();
            String busId = busIdField.getText();

            // Burada controller ya da DAO çağrısı yapılmalı (örnek/mock işlem):
            if (!origin.isEmpty() && !destination.isEmpty() && !dateTime.isEmpty() && !busId.isEmpty()) {
                messageLabel.setText("Sefer başarıyla eklendi!");
            } else {
                messageLabel.setText("Lütfen tüm alanları doldurun!");
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Kalkış:"), 0, 0);
        form.add(originField, 1, 0);
        form.add(new Label("Varış:"), 0, 1);
        form.add(destinationField, 1, 1);
        form.add(new Label("Tarih/Saat:"), 0, 2);
        form.add(dateTimeField, 1, 2);
        form.add(new Label("Otobüs ID:"), 0, 3);
        form.add(busIdField, 1, 3);
        form.add(addTripButton, 1, 4);
        form.add(messageLabel, 1, 5);

        root.getChildren().addAll(titleLabel, form);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}