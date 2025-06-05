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
import org.example.controller.ReservationController;
import org.example.models.UserModel;
import org.example.models.Trip;

public class UserDashboard {

    private final UserModel user;
    private final ReservationController reservationController;

    public ComboBox<String> departureBox;
    public ComboBox<String> arrivalBox;
    public DatePicker datePicker;
    public ToggleButton busButton;
    public ToggleButton planeButton;
    public VBox tripListBox;
    public Button reserveButton;
    public SeatLayout seatLayout;
    private boolean isBus;

    public boolean isBus() {
        return isBus;
    }

    public void setBus(boolean bus) {
        isBus = bus;
    }

    // Seçili sefer (tıklanınca güncellenir)
    public Trip selectedTrip = null;

    public UserDashboard(UserModel user) {
        this.user = user;
        this.reservationController = new ReservationController(this, user);
        show();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Kullanıcı Paneli");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#3b5998")),
                        new Stop(1, Color.web("#8b0033"))),
                CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Hoş geldiniz + Rezervasyonlarım butonu
        Label welcomeLabel = new Label("Hoş geldiniz, " + user.getUsername());
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        //rezervasyonların listelenceği buton
        Button myReservationsButton = new Button("Rezervasyonlarım");
        //buton controllera bağlanıyor
        myReservationsButton.setOnAction(e -> reservationController.handleListReservation(user)); // Dilersen ekleyebilirsin

        Region spacer = new Region(); // Boşluk bırakmak için
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox welcomeBox = new HBox(10, welcomeLabel, spacer, myReservationsButton);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);
        welcomeBox.setMaxWidth(Double.MAX_VALUE);

        Label infoLabel = new Label("Eposta: " + user.getEmail());
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        // Alanlar
        departureBox = new ComboBox<>();
        departureBox.getItems().addAll("İstanbul", "Ankara", "İzmir", "Antalya", "Bursa");
        departureBox.setPromptText("Kalkış ili");

        arrivalBox = new ComboBox<>();
        arrivalBox.getItems().addAll("İstanbul", "Ankara", "İzmir", "Antalya", "Bursa");
        arrivalBox.setPromptText("Varış ili");

        datePicker = new DatePicker();
        datePicker.setPromptText("Tarih seçin");

        ToggleGroup transportToggle = new ToggleGroup();
        busButton = new ToggleButton("🚌 Otobüs");
        planeButton = new ToggleButton("✈️ Uçak");
        busButton.setToggleGroup(transportToggle);
        planeButton.setToggleGroup(transportToggle);
        busButton.setSelected(true);
        busButton.setOnAction(e -> reservationController.handleBusSelected());
        planeButton.setOnAction(e -> reservationController.handlePlaneSelected());

        HBox transportSelector = new HBox(10, busButton, planeButton);
        transportSelector.setAlignment(Pos.CENTER);

        VBox selectionArea = new VBox(15,
                new VBox(6, new Label("Nereden"), departureBox),
                new VBox(6, new Label("Nereye"), arrivalBox),
                new VBox(6, new Label("Tarih"), datePicker)
        );
        selectionArea.setAlignment(Pos.CENTER);

        // Sefer Ara Butonu
        Button searchTripButton = new Button("Sefer Ara");

        Label tripLabel = new Label("Seferler:");
        tripLabel.setTextFill(Color.WHITE);
        tripLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        tripListBox = new VBox(15);
        tripListBox.setMaxWidth(450);

        reserveButton = new Button("Rezervasyon Yap");
        reserveButton.setDisable(true); // Başta disable

        //sefer arama butonunu controllera bağlar.
        searchTripButton.setOnAction(e -> reservationController.handleSearchTrips()); // sefer arama butonu

        //rezervasyon yapma butonunu controllera bağlar.
        reserveButton.setOnAction(e -> reservationController.handleReservation()); // rezervasyon yapma butonu

        root.getChildren().addAll(
                welcomeBox,
                infoLabel,
                transportSelector,
                selectionArea,
                searchTripButton,
                tripLabel,
                tripListBox,
                reserveButton
        );

        Scene scene = new Scene(root, 520, 700);
        stage.setScene(scene);
        stage.show();
    }

    // Sefer Kartına tıklandığında çağrılır
    public void setSelectedTrip(Trip trip, VBox selectedCard) {
        this.selectedTrip = trip;

        // Tüm kartların arka planını sıfırla
        for (javafx.scene.Node node : tripListBox.getChildren()) {
            if (node instanceof VBox) {
                ((VBox) node).setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

                // carda tıklandığında koltuk seçme ekranını aç.
                seatLayout = new SeatLayout(selectedTrip, user);
                Stage stage = new Stage();
                seatLayout.start(stage);
            }
        }
        // Seçili kartı vurgula
        selectedCard.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(12), Insets.EMPTY)));
        reserveButton.setDisable(false); // Butonu aktif et
    }
}
