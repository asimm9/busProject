package org.example.views;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
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

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // 🎨 Arka plan gradient
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#3b5998")),
                        new Stop(1, Color.web("#8b0033"))),
                CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // 🧾 Başlıklar
        Label welcomeLabel = new Label("Hoş geldiniz, " + user.getUsername());
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label infoLabel = new Label("Eposta: " + user.getEmail());
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        // 🧭 Nereden
        Label fromLabel = new Label("Nereden");
        fromLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        fromLabel.setTextFill(Color.web("#3b5998"));

        ComboBox<String> departureBox = new ComboBox<>();
        departureBox.getItems().addAll("İstanbul", "Ankara", "İzmir", "Antalya", "Bursa");
        departureBox.setPromptText("Kalkış ili");
        departureBox.setMaxWidth(Double.MAX_VALUE);
        departureBox.setPrefHeight(28);

        VBox fromCard = new VBox(6, fromLabel, departureBox);
        fromCard.setPadding(new Insets(8));
        fromCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        fromCard.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        fromCard.setMaxWidth(400);


        // 🗺️ Nereye
        Label toLabel = new Label("Nereye");
        toLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        toLabel.setTextFill(Color.web("#3b5998"));

        ComboBox<String> arrivalBox = new ComboBox<>();
        arrivalBox.getItems().addAll("İstanbul", "Ankara", "İzmir", "Antalya", "Bursa");
        arrivalBox.setPromptText("Varış ili");
        arrivalBox.setMaxWidth(Double.MAX_VALUE);
        arrivalBox.setPrefHeight(28);

        VBox toCard = new VBox(6, toLabel, arrivalBox);
        toCard.setPadding(new Insets(8));
        toCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        toCard.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        toCard.setMaxWidth(400);


        // 📅 Tarih
        Label dateLabel = new Label("Tarih");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        dateLabel.setTextFill(Color.web("#3b5998"));

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Tarih seçin");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setPrefHeight(28);

        VBox dateCard = new VBox(6, dateLabel, datePicker);
        dateCard.setPadding(new Insets(8));
        dateCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        dateCard.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        dateCard.setMaxWidth(400);

        // 🔘 Sefer tipi seçim butonları
        ToggleGroup transportToggle = new ToggleGroup();

        ToggleButton busButton = new ToggleButton("🚌 Otobüs");
        ToggleButton planeButton = new ToggleButton("✈️ Uçak");

        busButton.setToggleGroup(transportToggle);
        planeButton.setToggleGroup(transportToggle);
        busButton.setSelected(true);

// Başlangıç stilleri
        busButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        planeButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        busButton.setPrefWidth(100);
        planeButton.setPrefWidth(100);

// İlk seçimde görünüm
        busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");

// Seçim kontrolü
        busButton.setOnAction(e -> {
            busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
            planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
        });

        planeButton.setOnAction(e -> {
            planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
            busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
        });

        HBox transportSelector = new HBox(10, busButton, planeButton);
        transportSelector.setAlignment(Pos.CENTER);



        VBox selectionArea = new VBox(15, fromCard, toCard, dateCard);
        selectionArea.setAlignment(Pos.CENTER);

        // 🚌 Sefer Listesi
        Label tripLabel = new Label("Seferler:");
        tripLabel.setTextFill(Color.WHITE);
        tripLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        VBox tripListBox = new VBox(15);
        tripListBox.setMaxWidth(450);

        // 📋 Örnek sabit seferler
        String[][] trips = {
                {"İstanbul", "Ankara", "2024-06-01", "12:00"},
                {"Ankara", "İzmir", "2024-06-01", "15:00"},
                {"İzmir", "Antalya", "2024-06-01", "18:00"}
        };

        for (String[] trip : trips) {
            String departure = trip[0];
            String arrival = trip[1];
            String date = trip[2];
            String time = trip[3];

            Label cityLabel = new Label(departure + " → " + arrival);
            cityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            cityLabel.setTextFill(Color.web("#3b5998"));

            Label dateText = new Label("Tarih: " + date);
            Label timeText = new Label("Saat: " + time);
            dateText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            timeText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            dateText.setTextFill(Color.BLACK);
            timeText.setTextFill(Color.BLACK);

            HBox infoRow = new HBox(20, dateText, timeText);
            infoRow.setAlignment(Pos.CENTER_LEFT);

            VBox card = new VBox(8, cityLabel, infoRow);
            card.setPadding(new Insets(12));
            card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
            card.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));

            tripListBox.getChildren().add(card);
        }

        // 🔘 Buton
        Button reserveButton = new Button("Rezervasyon Yap");
        reserveButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        reserveButton.setTextFill(Color.web("#8b0033"));
        reserveButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        reserveButton.setPadding(new Insets(10, 25, 10, 25));
        reserveButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rezervasyon");
            alert.setHeaderText(null);
            alert.setContentText("Rezervasyon işlemi başlatıldı.");
            alert.showAndWait();
        });

        // ⬇️ Ekrana sırayla ekle
        root.getChildren().addAll(
                welcomeLabel,
                infoLabel,
                transportSelector, // ✅ işte bu yeni satır
                selectionArea,
                tripLabel,
                tripListBox,
                reserveButton
        );



        Scene scene = new Scene(root, 520, 700);
        stage.setScene(scene);
        stage.show();
    }
}




