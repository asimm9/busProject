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
import org.example.controller.AdminPanelController;
import org.example.models.UserModel;

public class AdminPanel {

    private final UserModel adminUser;
    private final AdminPanelController controller;

    // Form elemanları
    private TextField fromField;
    private TextField toField;
    private DatePicker datePicker;
    private TextField timeField;
    private TextField busIdField;
    private TextField planeIdField;
    private VBox busCard;
    private VBox planeCard;
    private Label messageLabel;
    private ToggleButton busButton;
    private ToggleButton planeButton;

    public AdminPanel(UserModel adminUser) {
        this.adminUser = adminUser;
        this.controller = new AdminPanelController(this);
        show();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Admin Paneli - Hoş geldin, " + adminUser.getUsername());

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // Arka plan (gradient)
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

        // Ulaşım Türü Seçimi (Otobüs / Uçak)
        ToggleGroup transportToggle = new ToggleGroup();

        busButton = new ToggleButton("🚌 Otobüs");
        planeButton = new ToggleButton("✈️ Uçak");
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

        // Kalkış ve Varış
        fromField = new TextField();
        VBox fromCard = createLabeledInput("Kalkış", fromField, "Kalkış Yeri");

        toField = new TextField();
        VBox toCard = createLabeledInput("Varış", toField, "Varış Yeri");

        // Tarih
        datePicker = new DatePicker();
        VBox dateCard = createLabeledInput("Tarih", datePicker);

        // Saat
        timeField = new TextField();
        VBox timeCard = createLabeledInput("Saat", timeField, "Örn: 13:30");

        // ID alanları
        busIdField = new TextField();
        busCard = createLabeledInput("Otobüs ID", busIdField, "Otobüs ID");

        planeIdField = new TextField();
        planeCard = createLabeledInput("Uçak ID", planeIdField, "Uçak ID");
        planeCard.setVisible(false);

        // Toggle dinamik kontrol (Controller'a delege edilecek)
        busButton.setOnAction(e -> controller.handleBusSelected());
        planeButton.setOnAction(e -> controller.handlePlaneSelected());

        // Sefer Ekleme Butonu
        Button addTripButton = new Button("Sefer Ekle");
        addTripButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addTripButton.setTextFill(Color.web("#8b0033"));
        addTripButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        addTripButton.setPadding(new Insets(10, 25, 10, 25));
        addTripButton.setOnAction(e -> controller.handleAddTrip());

        // Tüm Seferleri Listele Butonu
        Button listTripsButton = new Button("Tüm Seferleri Listele");
        listTripsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        listTripsButton.setTextFill(Color.web("#3b5998"));
        listTripsButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        listTripsButton.setPadding(new Insets(10, 25, 10, 25));
        listTripsButton.setOnAction(e -> controller.handleListTrips());

        messageLabel = new Label();
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setFont(Font.font("Arial", 13));

        StackPane transportIdStack = new StackPane(busCard, planeCard);

        formBox.getChildren().addAll(
                transportBox,
                fromCard,
                toCard,
                dateCard,
                timeCard,
                transportIdStack,
                addTripButton,
                listTripsButton, // <-- Burada ekledik
                messageLabel
        );

        root.getChildren().addAll(titleLabel, formBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Getterlar (Controller erişimi için)
    public TextField getFromField() { return fromField; }
    public TextField getToField() { return toField; }
    public DatePicker getDatePicker() { return datePicker; }
    public TextField getTimeField() { return timeField; }
    public TextField getBusIdField() { return busIdField; }
    public TextField getPlaneIdField() { return planeIdField; }
    public VBox getBusCard() { return busCard; }
    public VBox getPlaneCard() { return planeCard; }
    public Label getMessageLabel() { return messageLabel; }
    public ToggleButton getBusButton() { return busButton; }
    public ToggleButton getPlaneButton() { return planeButton; }

    // Ortak stil (TextField için)
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

    // Ortak stil (DatePicker için)
    private VBox createLabeledInput(String title, DatePicker picker) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#3b5998"));

        picker.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(6, label, picker);
        styleAsCard(box);
        return box;
    }

    // Kart görünümü
    private void styleAsCard(VBox card) {
        card.setPadding(new Insets(8));
        card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));
        card.setMaxWidth(400);
    }
}
