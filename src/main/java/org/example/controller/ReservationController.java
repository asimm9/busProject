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
import org.example.models.interfaces.EconomyClass;
import org.example.models.interfaces.VipClass;
import org.example.views.SeatLayout;
import org.example.views.UserDashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationController {

    private final UserDashboard view;
    private UserModel user;
    private final TripManager tripManager = TripManager.getInstance();
    private final ReservationManager reservationManager =  ReservationManager.getInstance();
    private final SeatManager seatManager = SeatManager.getInstance();
    private List<Seat> selectedSeats;


    public ReservationController(UserDashboard view, UserModel user) {
        this.view = view;
        this.user = user;

    }

    // Sefer arama işlemi
    public void handleSearchTrips() {
        String departure = view.departureBox.getValue();
        String arrival = view.arrivalBox.getValue();
        LocalDate selected = view.datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatted = selected == null ? "" : selected.format(formatter);
        VehicleType vehicleType;
        if(view.isBus()){
            vehicleType  = VehicleType.Bus;
        }else {
            vehicleType  = VehicleType.Plane;
        }

        if(departure.trim().isEmpty() || arrival.trim().isEmpty() || formatted.trim().isEmpty()){
            view.showAlert("Alanların hepsini doldurunuz.");
        }else{
            List<Trip> trips = tripManager.getTripByFilteredParameters(departure, arrival,formatted,vehicleType);
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
        if (view.seatLayout.selectedSeats.isEmpty()){
            view.showAlert("Koltuk Seçmediniz");
        }else {
            for (int i = 0; i< view.seatLayout.selectedSeats.size(); i++){
                if (Integer.parseInt(view.seatLayout.selectedSeats.get(i).getSeatID()) <= 10){
                    view.seatLayout.selectedSeats.get(i).setSeatClass(new EconomyClass());
                }else {
                    view.seatLayout.selectedSeats.get(i).setSeatClass(new VipClass());
                }

            }
            if (view.seatLayout.controller.manager.insertSeatsByTrip(view.seatLayout.selectedSeats)){
                if(view.seatLayout.secilenKoltuklar.size() != 0){




                    List<Seat> seatList = view.seatLayout.selectedSeats;
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
        }




    }

    //rezervasyonları listeleme için kullanılıyor.
    public void handleListReservation(UserModel user) {
        VehicleType vehicleType;
        vehicleType = view.isBus() == true ?  VehicleType.Bus : VehicleType.Plane;
        List<Reservation> reservationList = reservationManager.getReservations(user.getId(),vehicleType);

        if (reservationList == null || reservationList.isEmpty()) {
            showInfo("Hiç bir rezervasyon bulunamadı.");
            return;
        }

        Stage listReservationStage = new Stage();
        listReservationStage.setTitle("Tüm rezervasyonlar");

        VBox cardBox = new VBox(12);
        cardBox.setPadding(new Insets(15));

        // 🎨 Sayfa arka planı için GRADIENT stil eklendi
        cardBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f2f5, #e9eaf2);");

        for (Reservation reservation : reservationList) {
            if (reservation.getTrip().getVehicle() != null) {
                VBox card = createReservationCard(reservation);
                cardBox.getChildren().add(card);
            }else {
                continue;
            }
        }

        ScrollPane scrollPane = new ScrollPane(cardBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;"); // ScrollPane görünümünü de düzelt

        Scene scene = new Scene(scrollPane, 450, 500); // genişlik biraz artırıldı
        listReservationStage.setScene(scene);
        listReservationStage.show();
    }

    //liste içindeki her bir card bu metodla oluşturuluyor
    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(10); // spacing arttırıldı
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: #cccccc;" +
                "-fx-border-width: 1.2;" +
                "-fx-effect: dropshadow(gaussian, rgba(60,60,100,0.08), 10, 0, 0, 3);");

        card.setMaxWidth(440); // Genişlik artırıldı

        Label tripLabel = new Label(reservation.getTrip().getOrigin() + " → " + reservation.getTrip().getDestination());
        tripLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // kalın ve büyük yazı
        tripLabel.setTextFill(Color.web("#2a4d9b"));

        Label seatLabel = new Label("Koltuk Numarası: " + reservation.getSeat().getSeatID());
        seatLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        seatLabel.setTextFill(Color.web("#3b5998"));

        Label dateLabel = new Label("Tarih: " + reservation.getTrip().getDepartureTime());
        dateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        dateLabel.setTextFill(Color.web("#444444"));

        Label timeLabel = new Label("Saat: " + reservation.getTrip().getTime());
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        timeLabel.setTextFill(Color.web("#444444"));


        Label seatType = new Label("Sınıfınız: "+ reservation.getSeat().getSeatClass().getClassName() + "  Ücret: "
                + reservation.getSeat().getSeatClass().getPrice(reservation.getTrip().getVehicle().getPrice()) + " TL" );
        seatType.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        seatType.setTextFill(Color.web("#444444"));

        card.getChildren().addAll(tripLabel, seatLabel, dateLabel, timeLabel, seatType);

        // Sürükleme işlemleri
        final double[] mouseAnchorX = new double[1];

        card.setOnMousePressed(event -> {
            mouseAnchorX[0] = event.getSceneX();
        });

        card.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseAnchorX[0];
            if (deltaX < 0) {
                card.setTranslateX(deltaX);
            }
        });

        card.setOnMouseReleased(event -> {
            double deltaX = card.getTranslateX();
            if (deltaX < -120) {
                ((VBox) card.getParent()).getChildren().remove(card);
                List<Seat> toUpdatedSeat= new ArrayList<>();
                Seat seat = reservation.getSeat();
                seat.setReserved(false);
                seat.setTripID(null);
                seat.setUserID(null);
                toUpdatedSeat.add(seat);
                if (reservationManager.cancelReservation(reservation) && seatManager.insertSeatsByTrip(toUpdatedSeat)) {
                    System.out.println("silindiiiiiiiiiiii");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rezervasyon Silindi");
                alert.setHeaderText(null);
                alert.setContentText("Rezervasyon başarıyla silindi.");
                alert.showAndWait();
            } else {
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

    //çıkış işlemi burda yapılıyor.
    public void handleLogout() {
        user = null;
        Stage currentStage = (Stage) view.logoutButton.getScene().getWindow();
        currentStage.close();
    }

    //uçak ve otobüs arasındaki seçimle bu iki metodda ele alınıyor
    public void handleBusSelected(){
        view.setBus(true);
        view.busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
    }
    public void handlePlaneSelected(){
        view.planeButton.setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.busButton.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
        view.setBus(false);
    }

}
