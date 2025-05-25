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

public class AdminPanel {

    private final UserModel adminUser;

    public AdminPanel(UserModel adminUser) {
        this.adminUser = adminUser;
        show();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Admin Paneli - Hoş geldin, " + adminUser.getUsername());

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // 🎨 Arka plan (gradient)
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#3b5998")),
                        new Stop(1, Color.web("#8b0033"))),
                CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        Label titleLabel = new Label("Sefer Ekle");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        VBox formBox = new VBox(15);
        formBox.setMaxWidth(400);

        // 🔘 Ulaşım Türü Seçimi (Otobüs / Uçak)
        ToggleGroup transportToggle = new ToggleGroup();

        ToggleButton busButton = new ToggleButton("🚌 Otobüs");
        ToggleButton planeButton = new ToggleButton("✈️ Uçak");
        busButton.setToggleGroup(transportToggle);
        planeButton.setToggleGroup(transportToggle);
        busButton.setSelected(true);

        busButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        planeButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        busButton.setPrefWidth(100);
        planeButton.setPrefWidth(100);
        busButton.setStyle("-fx-background-radius: 10; -fx-background-color: white; -fx-text-fill: #8b0033;");
        planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: white; -fx-text-fill: #8b0033;");

        HBox transportBox = new HBox(10, busButton, planeButton);
        transportBox.setAlignment(Pos.CENTER);

        // 🚌 Kalkış
        VBox fromCard = createLabeledInput("Kalkış", new TextField(""), "Kalkış Yeri");

        // 🗺️ Varış
        VBox toCard = createLabeledInput("Varış", new TextField(""), "Varış Yeri");

        // 📅 Tarih
        DatePicker datePicker = new DatePicker();
        VBox dateCard = createLabeledInput("Tarih", datePicker);

        // 🕒 Saat
        TextField timeField = new TextField();
        VBox timeCard = createLabeledInput("Saat", timeField, "Örn: 13:30");

        // 🆔 Otobüs/Uçak ID (gizli gösterilecek)
        TextField busIdField = new TextField();
        VBox busCard = createLabeledInput("Otobüs ID", busIdField, "Otobüs ID");

        TextField planeIdField = new TextField();
        VBox planeCard = createLabeledInput("Uçak ID", planeIdField, "Uçak ID");
        planeCard.setVisible(false); // başlangıçta sadece otobüs görünür

        // Toggle dinamik kontrol
        // Başlangıçta butonların stilleri
        busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");


// Otobüs seçilince
        busButton.setOnAction(e -> {
            busCard.setVisible(true);
            planeCard.setVisible(false);

            busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
            planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
        });

        planeButton.setOnAction(e -> {
            busCard.setVisible(false);
            planeCard.setVisible(true);

            planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
            busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
        });



        // ✅ Sefer Ekleme Butonu
        Button addTripButton = new Button("Sefer Ekle");
        addTripButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addTripButton.setTextFill(Color.web("#8b0033"));
        addTripButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        addTripButton.setPadding(new Insets(10, 25, 10, 25));

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setFont(Font.font("Arial", 13));

        addTripButton.setOnAction(e -> {
            String origin = ((TextField) fromCard.getChildren().get(1)).getText();
            String destination = ((TextField) toCard.getChildren().get(1)).getText();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String time = timeField.getText();
            String id = busCard.isVisible() ? busIdField.getText() : planeIdField.getText();

            if (!origin.isEmpty() && !destination.isEmpty() && !date.isEmpty() && !time.isEmpty() && !id.isEmpty()) {
                messageLabel.setText("Sefer başarıyla eklendi!");
            } else {
                messageLabel.setText("Lütfen tüm alanları doldurun!");
            }
        });

        // 👇 Tüm formu sırala
        // 🔁 ID alanları sabit konumda dönüşümlü gösterilecek
        StackPane transportIdStack = new StackPane(busCard, planeCard);
        planeCard.setVisible(false);

        formBox.getChildren().addAll(
                transportBox,
                fromCard,
                toCard,
                dateCard,
                timeCard,
                transportIdStack, // sadece bu eklendi
                addTripButton,
                messageLabel
        );


        root.getChildren().addAll(titleLabel, formBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    // 📦 Ortak stil (TextField için)
    private VBox createLabeledInput(String title, TextField input, String prompt) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#3b5998"));

        input.setPromptText(prompt);
        input.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(6, label, input);
        styleAsCard(box);
        return box;
    }

    // 📦 Ortak stil (DatePicker için)
    private VBox createLabeledInput(String title, DatePicker picker) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#3b5998"));

        picker.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(6, label, picker);
        styleAsCard(box);
        return box;
    }

    // 💄 Kart görünümü
    private void styleAsCard(VBox card) {
        card.setPadding(new Insets(8));
        card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));
        card.setMaxWidth(400);
    }
}
