package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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
    public Button logoutButton;
    private boolean isBus;

    //private değişkenlerin getter ve setterları
    public boolean isBus() {return isBus;}
    public void setBus(boolean bus) {isBus = bus;}// Getterlar (Controller erişimi için)
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


    //sınıfın constructoru ve controllera bağlandığı nokta
    public AdminPanel(UserModel adminUser) {
        this.adminUser = adminUser;
        this.controller = new AdminPanelController(this,adminUser);
        setBus(true);
        show();
    }


    //sayfanın build edildiği metod burası.
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


        //sayfa başlığı
        Label titleLabel = new Label("Sefer Ekle");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));


        VBox formBox = new VBox(15);
        formBox.setMaxWidth(500);

        // Ulaşım Türü Seçimi (Otobüs / Uçak)
        ToggleGroup transportToggle = new ToggleGroup();
        busButton = new ToggleButton("🚌 Otobüs");
        planeButton = new ToggleButton("✈️ Uçak");
        busButton.setToggleGroup(transportToggle);
        planeButton.setToggleGroup(transportToggle);

        busButton.setSelected(true);
        planeButton.setSelected(false);
        busButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        planeButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        busButton.setPrefWidth(100);
        planeButton.setPrefWidth(100);
        busButton.setStyle("-fx-background-radius: 10; -fx-background-color: white; -fx-text-fill: #8b0033;");
        planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: white; -fx-text-fill: #8b0033;");
        HBox transportBox = new HBox(10, busButton, planeButton);
        transportBox.setAlignment(Pos.CENTER);

        //Kalkış formu.
        fromField = new TextField();
        VBox fromCard = createLabeledInput("Kalkış", fromField, "Kalkış Yeri");
        fromCard.setAlignment(Pos.CENTER);
        fromCard.setMaxWidth(Double.MAX_VALUE);

        //varış formu.
        toField = new TextField();
        VBox toCard = createLabeledInput("Varış", toField, "Varış Yeri");
        toCard.setAlignment(Pos.CENTER);
        toCard.setMaxWidth(Double.MAX_VALUE);


        //Tarih seçme formu
        datePicker = new DatePicker();
        VBox dateCard = createLabeledInput("Tarih", datePicker);
        dateCard.setAlignment(Pos.CENTER);
        dateCard.setMaxWidth(Double.MAX_VALUE);


        //Saat seçme formu.
        timeField = new TextField();
        VBox timeCard = createLabeledInput("Saat", timeField, "Örn: 13:30");
        timeCard.setAlignment(Pos.CENTER);
        timeCard.setMaxWidth(Double.MAX_VALUE);



        // Otobüs ise otobüs plakası
        busIdField = new TextField();
        busCard = createLabeledInput("Otobüs Plakası", busIdField, "Otobüs Plakası");

        //Uçak ise uçak id belirleme kısmı
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
        //metod controllera burda bağlanıyor.
        addTripButton.setOnAction(e -> controller.handleAddTrip());

        // Tüm Seferleri Listele Butonu
        Button listTripsButton = new Button("Tüm Seferleri Listele");
        listTripsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        listTripsButton.setTextFill(Color.web("#3b5998"));
        listTripsButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        listTripsButton.setPadding(new Insets(10, 25, 10, 25));
        //buton controllera burda bağlanıyor.
        listTripsButton.setOnAction(e -> controller.handleListTrips());

        //Bus Ekleme butonu
        Button addBusButton = new Button("Araç Ekle");
        addBusButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addBusButton.setTextFill(Color.web("#8b0033"));
        addBusButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        addBusButton.setPadding(new Insets(10, 25, 10, 25));
        //buton kontrollera burda bağlanıyor.
        addBusButton.setOnAction(actionEvent -> controller.handleInsertVeihcle());

        //busları listleme butonu
        Button listBusesButton = new Button("Araçları Listele");
        listBusesButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        listBusesButton.setTextFill(Color.web("#3b5998"));
        listBusesButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        listBusesButton.setPadding(new Insets(10, 25, 10, 25));
        //buton controllera burda bağlanıyor.
        listBusesButton.setOnAction(actionEvent -> controller.handleListBuses());

        // Sefer işlemleri (Sol taraf)
        VBox tripBox = new VBox(10, addTripButton, listTripsButton);
        tripBox.setAlignment(Pos.CENTER);

        // Otobüs işlemleri (Sağ taraf)
        VBox busBox = new VBox(10, addBusButton, listBusesButton);
        busBox.setAlignment(Pos.CENTER);

        // Dikey ayraç
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setPrefHeight(70); // İstersen boyunu ayarla

        // Hepsini bir HBox'ta topla
        HBox buttonRow = new HBox(20, tripBox, separator, busBox);
        buttonRow.setAlignment(Pos.CENTER);

        messageLabel = new Label();
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setFont(Font.font("Arial", 13));

        StackPane transportIdStack = new StackPane(busCard, planeCard);
        StackPane.setAlignment(busCard, Pos.CENTER);
        StackPane.setAlignment(planeCard, Pos.CENTER);

        busCard.setMaxWidth(Double.MAX_VALUE);
        planeCard.setMaxWidth(Double.MAX_VALUE);

        logoutButton = new Button("Çıkış Yap");
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        logoutButton.setTextFill(Color.web("#8b0033"));
        logoutButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        logoutButton.setPadding(new Insets(10, 25, 10, 25));
        logoutButton.setOnAction(actionEvent -> controller.handleLogout());

        formBox.getChildren().addAll(
                transportBox,
                fromCard,
                toCard,
                dateCard,
                timeCard,
                transportIdStack,
                buttonRow,
                messageLabel,
                logoutButton
        );

        root.getChildren().addAll(titleLabel, formBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }



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
