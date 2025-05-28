package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
import org.example.managers.ReservationManager;
import org.example.managers.TripManager;
import org.example.models.Reservation;
import org.example.models.Seat;
import org.example.models.Trip;
import org.example.models.UserModel;
import org.example.views.SeatLayout;
import org.example.views.UserDashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReservationController {

    private final UserDashboard view;
    private final UserModel user;
    private final TripManager tripManager = new TripManager();
    private final ReservationManager reservationManager =  ReservationManager.getInstance();


    public ReservationController(UserDashboard view, UserModel user) {
        this.view = view;
        this.user = user;

    }

    // Sefer arama işlemi
    public void handleSearchTrips() {
        String departure = view.departureBox.getValue();
        String arrival = view.arrivalBox.getValue();
        LocalDate date = view.datePicker.getValue();
        String type = view.busButton.isSelected() ? "Otobüs" : "Uçak";

        List<Trip> trips = tripManager.getTripByFilteredParameters(departure, arrival);

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

            Reservation reservation = new Reservation();
            reservation.setId(UUID.randomUUID().toString());
            reservation.setUser(user);
            reservation.setTrip(selected);
            Seat seat = new Seat();
            reservation.setSeat(seat);
            LocalDateTime dateTime = LocalDateTime.now();
            reservation.setReservationDateTime(dateTime);
            reservationManager.createReservation(reservation);
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
}
