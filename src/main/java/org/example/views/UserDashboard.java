package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.models.Trip;
import org.example.models.UserModel;
import org.example.views.SeatLayout;


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
    public Button logoutButton;
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

        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // Arka plan: gradient
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#3b5998")),
                        new Stop(1, Color.web("#8b0033"))),
                CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Başlık ve Rezervasyonlarım butonu
        Label welcomeLabel = new Label("Hoş geldiniz, " + user.getUsername());
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Button myReservationsButton = new Button("Rezervasyonlarım");
        myReservationsButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        myReservationsButton.setTextFill(Color.web("#8b0033"));
        myReservationsButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        myReservationsButton.setPadding(new Insets(8, 18, 8, 18));
        myReservationsButton.setOnAction(e -> reservationController.handleListReservation(user));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox welcomeBox = new HBox(10, welcomeLabel, spacer, myReservationsButton);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);
        welcomeBox.setMaxWidth(Double.MAX_VALUE);

        // Kullanıcı bilgisi
        Label infoLabel = new Label("Eposta: " + user.getEmail());
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        // Ulaşım türü (otobüs / uçak)
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

        busButton.setOnAction(e -> reservationController.handleBusSelected());
        planeButton.setOnAction(e -> reservationController.handlePlaneSelected());

        HBox transportSelector = new HBox(10, busButton, planeButton);
        transportSelector.setAlignment(Pos.CENTER);

        // 🔽 81 ili tanımla
        ObservableList<String> cities = FXCollections.observableArrayList(
                "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın",
                "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum",
                "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun",
                "Gümüşhane", "Hakkâri", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri",
                "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin",
                "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas",
                "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray",
                "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük",
                "Kilis", "Osmaniye", "Düzce"
        );

        // Kalkış ili
        departureBox = new ComboBox<>();
        departureBox.setEditable(false);
        departureBox.setPromptText("Kalkış ili");
        departureBox.setItems(cities);
        departureBox.setMaxWidth(Double.MAX_VALUE);

        // Varış ili
        arrivalBox = new ComboBox<>();
        arrivalBox.setEditable(false);
        arrivalBox.setPromptText("Varış ili");
        arrivalBox.setItems(cities);
        arrivalBox.setMaxWidth(Double.MAX_VALUE);

        // Tarih
        datePicker = new DatePicker();
        datePicker.setPromptText("Tarih seçin");
        datePicker.setMaxWidth(Double.MAX_VALUE);

        // Form alanlarını kart stiline sok
        VBox selectionArea = new VBox(15,
                styleAsCard("Nereden", departureBox),
                styleAsCard("Nereye", arrivalBox),
                styleAsCard("Tarih", datePicker)
        );
        selectionArea.setAlignment(Pos.CENTER);
        selectionArea.setMaxWidth(400);

        // Sefer ara butonu
        Button searchTripButton = new Button("Sefer Ara");
        searchTripButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        searchTripButton.setTextFill(Color.web("#8b0033"));
        searchTripButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        searchTripButton.setPadding(new Insets(10, 25, 10, 25));
        searchTripButton.setOnAction(e -> reservationController.handleSearchTrips());

        // Seferler başlığı
        Label tripLabel = new Label("Seferler:");
        tripLabel.setTextFill(Color.WHITE);
        tripLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Sefer kutuları
        tripListBox = new VBox(15);
        tripListBox.setMaxWidth(450);

        // Rezervasyon butonu
        reserveButton = new Button("Rezervasyon Yap");
        reserveButton.setDisable(true);
        reserveButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        reserveButton.setTextFill(Color.WHITE);
        reserveButton.setBackground(new Background(new BackgroundFill(Color.web("#8b0033"), new CornerRadii(12), Insets.EMPTY)));
        reserveButton.setPadding(new Insets(10, 25, 10, 25));
        reserveButton.setOnAction(e -> reservationController.handleReservation());

        logoutButton = new Button("Çıkış Yap");
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        logoutButton.setTextFill(Color.web("#8b0033"));
        logoutButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        logoutButton.setPadding(new Insets(10, 25, 10, 25));
        logoutButton.setOnAction(actionEvent -> reservationController.handleLogout());

        // Ekrana ekle
        root.getChildren().addAll(
                welcomeBox,
                infoLabel,
                transportSelector,
                selectionArea,
                searchTripButton,
                tripLabel,
                tripListBox,
                reserveButton,
                logoutButton
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

    private VBox styleAsCard(String labelText, Control input) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#3b5998"));

        input.setMaxWidth(Double.MAX_VALUE);

        VBox box = new VBox(6, label, input);
        box.setPadding(new Insets(10));
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));
        box.setMaxWidth(400);
        return box;
    }


    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
