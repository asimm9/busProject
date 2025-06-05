package org.example.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.managers.ReservationManager;
import org.example.managers.SeatManager;
import org.example.managers.TripManager;
import org.example.models.*;
import org.example.views.SeatLayout;
import org.example.views.UserDashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReservationController {

    private final UserDashboard view;
    private final UserModel user;
    private final TripManager tripManager = TripManager.getInstance();
    private final ReservationManager reservationManager =  ReservationManager.getInstance();
    private final SeatManager seatManager = SeatManager.getInstance();


    public ReservationController(UserDashboard view, UserModel user) {
        this.view = view;
        this.user = user;

    }

    // Sefer arama işlemi
    public void handleSearchTrips() {
        String departure = view.departureBox.getValue();
        String arrival = view.arrivalBox.getValue();
        LocalDate date = view.datePicker.getValue();
        VehicleType vehicleType;
        if(view.isBus()){
            vehicleType  = VehicleType.Bus;
        }else {
            vehicleType  = VehicleType.Plane;
        }


        List<Trip> trips = tripManager.getTripByFilteredParameters(departure, arrival,vehicleType);

        VBox tripListBox = view.tripListBox;
        tripListBox.getChildren().clear();
        view.selectedTrip = null;
        view.reserveButton.setDisable(true); // Kart seçilmeden rezerve olmasın

        if (trips.isEmpty()) {
            Label noResult = new Label("Uygun sefer bulunamadı.");
            noResult.setTextFill(Color.WHITE);
            noResult.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            tripListBox.getChildren().add(noResult);
        } else {
            for (Trip trip : trips) {
                Label cityLabel = new Label(trip.getOrigin() + " → " + trip.getDestination());
                cityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                cityLabel.setTextFill(Color.web("#3b5998"));

                Label dateText = new Label("Tarih: " + trip.getDepartureTime());
                Label timeText = new Label("Saat: " + trip.getTime());
                dateText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                timeText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                dateText.setTextFill(Color.BLACK);
                timeText.setTextFill(Color.BLACK);

                HBox infoRow = new HBox(20, dateText, timeText);
                infoRow.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(8, cityLabel, infoRow);
                card.setPadding(new Insets(12));
                card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
                card.setBorder(null);
                card.setCursor(javafx.scene.Cursor.HAND);

                // TIKLANABİLİR YAPI!
                card.setOnMouseClicked(e -> view.setSelectedTrip(trip, card));

                tripListBox.getChildren().add(card);
            }
        }
    }

    // Rezervasyon işlemi
    public void handleReservation() {
        Trip selected = view.selectedTrip;
        if (selected == null) {
            // Kural gereği buraya düşmez, buton disable
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen bir sefer seçin.");
            alert.showAndWait();
            return;
        }
        // İstediğin işlemi burada yapabilirsin (veritabanına kayıt vs.)


        if(view.seatLayout.secilenKoltuklar.size() != 0){

            List<Seat> seatList = seatManager.getSeatByTripAndUserID(selected.getTripID(),user.getId());
            for (int i = 0; i < seatList.size(); i++) {
                Seat seat = seatList.get(i);
                Reservation reservation = new Reservation();
                reservation.setId(UUID.randomUUID().toString());
                reservation.setUser(user);
                reservation.setTrip(selected);
                reservation.setSeat(seat);
                LocalDateTime dateTime = LocalDateTime.now();
                reservation.setReservationDateTime(dateTime);
                reservationManager.createReservation(reservation);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rezervasyon");
            alert.setHeaderText(null);
            alert.setContentText(
                    selected.getOrigin() + " → " + selected.getDestination() +
                            "\nTarih: " + selected.getDepartureTime() + " Saat: " + selected.getTime() +
                            "\nRezervasyon işlemi başarılı!"
            );

            alert.showAndWait();
        }


    }

    public void handleCancelReservation() {

    }

    public void handleListReservation(UserModel user) {
        List<Reservation> reservationList =  reservationManager.getReservations(user.getId());

        if(reservationList == null || reservationList.isEmpty()){
            showInfo("Hiç bir rezervasyon bulunamadı.");
            return;
        }

        Stage listReservationStage = new Stage();
        listReservationStage.setTitle("Tüm rezervasyonlar.");

        VBox cardBox = new VBox(12);
        cardBox.setPadding(new Insets(15));
        cardBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f2f5, #e9eaf2);");

        for (Reservation reservation : reservationList) {
            VBox card = createReservationCard(reservation);
            cardBox.getChildren().add(card);
        }

        Scene scene = new Scene(new ScrollPane(cardBox), 430, 450);
        listReservationStage.setScene(scene);
        listReservationStage.show();
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(7);
        card.setPadding(new Insets(12));
        card.setSpacing(5);
        card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-border-color: #d2d2d2;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(60,60,100,0.08), 8,0,0,2);");

        card.setMaxWidth(360);

        Label userID = new Label(reservation.getUser().getId());
        userID.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userID.setTextFill(Color.web("#3b5998"));

        Label tripLabel = new Label(reservation.getTrip().getOrigin() + " => " + reservation.getTrip().getDestination());
        tripLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        tripLabel.setTextFill(Color.web("#3b5998"));

        Label seatLabel = new Label(" null değer");
        seatLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        seatLabel.setTextFill(Color.web("#3b5998"));

        card.getChildren().addAll(userID, tripLabel, seatLabel);

        // Sürükleme için değişkenler
        final double[] mouseAnchorX = new double[1];

        card.setOnMousePressed(event -> {
            mouseAnchorX[0] = event.getSceneX();
        });

        card.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseAnchorX[0];
            if (deltaX < 0) { // sola sürükleniyorsa
                card.setTranslateX(deltaX);
            }
        });

        card.setOnMouseReleased(event -> {
            double deltaX = card.getTranslateX();
            if (deltaX < -120) { // Eşik değeri, yeterince sola sürüklediyse sil
                // Önce UI'dan kaldır
                ((VBox) card.getParent()).getChildren().remove(card);

                // Sonra veri kaynağından sil
                if (reservationManager.cancelReservation(reservation)) {
                    System.out.println("silindiiiiiiiiiiii");
                } // TripManager'da bu metodu eklemelisin

                // İsteğe bağlı: "Sefer silindi" mesajı göster
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rezervasyon Silindi");
                alert.setHeaderText(null);
                alert.setContentText("Rezervasyon başarıyla silindi.");
                alert.showAndWait();
            } else {
                // Yeterince sürüklenmediyse kart eski yerine dönsün
                card.setTranslateX(0);
            }
        });

        return card;
    }

    private void showInfo(String text) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Tüm reservasyonlar");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void handleBusSelected(){
        view.setBus(true);
    }

    public void handlePlaneSelected(){
        view.setBus(false);
    }
}
