package org.example.views;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.models.UserModel;

public class UserDashboard {

    private final UserModel user;

    public UserDashboard(UserModel user) {
        this.user = user;
        show();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Kullanıcı Paneli");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Hoş geldiniz, " + user.getUsername());
        Label infoLabel = new Label("Eposta: " + user.getEmail());

        // Sefer listesi (örnek)
        TableView<String> tripTable = new TableView<>();
        TableColumn<String, String> tripColumn = new TableColumn<>("Sefer Bilgisi");
        tripColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

        ObservableList<String> tripData = FXCollections.observableArrayList(
                "İstanbul - Ankara - 2024-06-01 12:00",
                "Ankara - İzmir - 2024-06-01 15:00",
                "İzmir - Antalya - 2024-06-01 18:00"
        );

        tripTable.setItems(tripData);
        tripTable.getColumns().add(tripColumn);
        tripTable.setPrefHeight(150);

        Button reserveButton = new Button("Rezervasyon Yap");
        reserveButton.setOnAction(e -> {
            String selectedTrip = tripTable.getSelectionModel().getSelectedItem();
            if (selectedTrip != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rezervasyon");
                alert.setHeaderText(null);
                alert.setContentText("Seçilen sefer: " + selectedTrip + "\nRezervasyon işlemi başlatıldı.");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(welcomeLabel, infoLabel, new Label("Seferler:"), tripTable, reserveButton);

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
}