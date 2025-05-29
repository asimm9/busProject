package org.example.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.managers.BusManager;
import org.example.managers.SeatManager;
import org.example.managers.TripManager;
import org.example.models.Bus;
import org.example.models.Seat;
import org.example.models.Trip;
import org.example.views.AdminPanel;
import javafx.scene.layout.VBox.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdminPanelController {

    private final AdminPanel view;
    private TripManager tripManager = TripManager.getInstance();
    private BusManager busManager = BusManager.getInstance();
    private SeatManager seatManager = SeatManager.getInstance();

    //viewa bağlanma kısmı controller constructerında yapılıyor.
    public AdminPanelController(AdminPanel view) {
        this.view = view;
    }

    //Otobüs sekmesinde seçili olduğumuzu gösteriyor
    public void handleBusSelected() {
        view.getBusCard().setVisible(true);
        view.getPlaneCard().setVisible(false);

        view.getBusButton().setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.getPlaneButton().setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
    }

    //Uçak sekmesinde seçili olduğunu gösteriyor
    public void handlePlaneSelected() {
        view.getBusCard().setVisible(false);
        view.getPlaneCard().setVisible(true);

        view.getPlaneButton().setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.getBusButton().setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
    }

    //adminin yeni bir trip ekleme işi burdan yapılıyor
    public void handleAddTrip() {
        String origin = view.getFromField().getText();
        String destination = view.getToField().getText();
        DatePicker date = view.getDatePicker() == null ? new DatePicker() : view.getDatePicker();
        LocalDateTime now = LocalDateTime.now();
        String busID = view.getBusCard().isVisible() ? view.getBusIdField().getText() : view.getPlaneIdField().getText();

        if (!origin.isEmpty() && !destination.isEmpty() /*&& !date.isEmpty() && !now.isEmpty()*/ && !busID.isEmpty()) {
            Trip trip = new Trip();
            Bus bus = new Bus();
            bus.setBusID(busID);
            bus.setBusType("s");
            bus.setSeatLayout(null);
            bus.setTotalSeats(23);

            trip.setTripID(UUID.randomUUID().toString());
            trip.setOrigin(origin);
            trip.setDestination(destination);
            trip.setDepartureTime(date);
            trip.setTime(now);
            trip.setBus(bus);
            if (tripManager.createTrip(trip)){
                view.getMessageLabel().setText("Sefer başarıyla eklendi!");
            }else {
                view.getMessageLabel().setText("Trip oluşturulamadı.");
            }
        } else {
            view.getMessageLabel().setText("Lütfen tüm alanları doldurun!");


        }
    }

    //tüm tripleri listelediğimiz metod
    public void handleListTrips() {
        java.util.List<Trip> tripList = tripManager.getAllTrips();

        if (tripList == null || tripList.isEmpty()) {
            showInfo("Hiç sefer bulunamadı.");
            return;
        }

        // Kart penceresi oluştur
        javafx.stage.Stage listStage = new javafx.stage.Stage();
        listStage.setTitle("Tüm Seferler");

        javafx.scene.layout.VBox cardsBox = new javafx.scene.layout.VBox(12);
        cardsBox.setPadding(new javafx.geometry.Insets(15));
        cardsBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f2f5, #e9eaf2);");

        for (Trip trip : tripList) {
            VBox card = createTripCard(trip);
            cardsBox.getChildren().add(card);
        }

        javafx.scene.Scene scene = new javafx.scene.Scene(new javafx.scene.control.ScrollPane(cardsBox), 430, 450);
        listStage.setScene(scene);
        listStage.show();
    }

    //cardın ui ı burda olur ve silinme işlemi de sola sürüklemeli oluyo metod burda işleniyor
    private VBox createTripCard(Trip trip) {
        VBox card = new VBox(7);
        card.setPadding(new javafx.geometry.Insets(12));
        card.setSpacing(5);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: #d2d2d2;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(60,60,100,0.08), 8,0,0,2);"
        );
        card.setMaxWidth(360);

        Label idLabel = new Label("ID: " + trip.getTripID());
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        idLabel.setTextFill(Color.web("#3b5998"));

        Label fromTo = new Label("Kalkış: " + trip.getOrigin() + "   ➔   Varış: " + trip.getDestination());
        fromTo.setFont(Font.font("Arial", 13));

        Label dateLabel = new Label("Tarih & Saat: " + (trip.getDepartureTime() != null ? trip.getDepartureTime().toString() : "-"));
        dateLabel.setFont(Font.font("Arial", 12));

        String transportInfo = trip.getBus().toString();
        Label transportLabel = new Label(transportInfo);
        transportLabel.setFont(Font.font("Arial", 12));
        transportLabel.setTextFill(Color.web("#8b0033"));

        card.getChildren().addAll(idLabel, fromTo, dateLabel, transportLabel);

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
                if (tripManager.deleteTrip(trip)){
                    System.out.println("silindiiiiiiiiiiii");
                } // TripManager'da bu metodu eklemelisin

                // İsteğe bağlı: "Sefer silindi" mesajı göster
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sefer Silindi");
                alert.setHeaderText(null);
                alert.setContentText("Sefer başarıyla silindi.");
                alert.showAndWait();
            } else {
                // Yeterince sürüklenmediyse kart eski yerine dönsün
                card.setTranslateX(0);
            }
        });

        return card;
    }

    // Bilgi penceresi (liste boşsa)
    private void showInfo(String text) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Tüm Seferler");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    // Bus eklemek için bu metod kullanılıyor
    public void handleInsertBus() {

        Stage formStage = new Stage();
        formStage.setTitle("Otobüs Ekle");

        // Form elemanlarını oluştur
        Label plakaLabel = new Label("Plaka:");
        TextField idField = new TextField();

        Label koltukLabel = new Label("type");
        TextField busTypeField = new TextField();

        Label totalSeatsLabel = new Label("Total Seats:");
        TextField totalSeatsField = new TextField();
        final boolean[] result = new boolean[1];
        Button saveButton = new Button("Kaydet");
        saveButton.setOnAction(event -> {
            String id = idField.getText();
            String busType = busTypeField.getText();
            String totalSeats = totalSeatsField.getText();
            Bus bus = new Bus();
            bus.setBusType(busType);
            bus.setBusID(id);

            bus.setTotalSeats(Integer.parseInt(totalSeats));
            int row= 0;
            int column = 0;
            Seat[][] seats;
            if(Integer.parseInt(totalSeats) == 36){
                if (busType.equals("2+2")){//2+2
                    seats = new Seat[9][4];
                    int seatNumber= 1;
                    for (int rowCount = 0; rowCount < 9; rowCount++) {
                        for (int columnCount = 0; columnCount < 4; columnCount++) {
                            Seat seat = new Seat();
                            seat.setColumn(columnCount);
                            seat.setRow(rowCount);
                            seat.setReserved(false);

                            seat.setSeatID(String.valueOf(seatNumber));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setBusID(id);
                            seats[rowCount][columnCount] = seat;
                            seatNumber++;
                        }
                    }
                    seatManager.insertSeatByBus(seats);
                } else  {
                    seats = new Seat[12][3];//2+1
                    int seatNumber= 1;
                    for (int rowCount = 0; rowCount < 12; rowCount++) {
                        for (int columnCount = 0; columnCount < 3; columnCount++) {
                            Seat seat = new Seat();
                            seat.setColumn(columnCount);
                            seat.setRow(rowCount);
                            seat.setReserved(false);

                            seat.setSeatID(String.valueOf(seatNumber));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setBusID(id);
                            seats[rowCount][columnCount] = seat;
                        seatNumber++;
                        }
                    }
                    seatManager.insertSeatByBus(seats);
                }
            } else  {
                if (busType.equals("2+2")){//48
                    seats = new Seat[12][4];
                    int seatNumber= 1;
                    for (int rowCount = 0; rowCount < 12; rowCount++) {
                        for (int columnCount = 0; columnCount < 4; columnCount++) {
                            Seat seat = new Seat();
                            seat.setColumn(columnCount);
                            seat.setRow(rowCount);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setBusID(id);
                            seats[rowCount][columnCount] = seat;
                            seatNumber++;
                        }
                    }
                    seatManager.insertSeatByBus(seats);

                } else  {
                    seats = new Seat[16][3];//2+1
                    int seatNumber= 1;
                    for (int rowCount = 0; rowCount < 16; rowCount++) {
                        for (int columnCount = 0; columnCount < 3; columnCount++) {
                            Seat seat = new Seat();
                            seat.setColumn(columnCount);
                            seat.setRow(rowCount);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setBusID(id);
                            seats[rowCount][columnCount] = seat;
                            seatNumber++;
                        }
                    }
                    seatManager.insertSeatByBus(seats);

                }
            }



            bus.setSeatLayout(new Seat[3][3]);
             if(busManager.insertBus(bus)){
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Bus Eklendi");
                 formStage.close();
             }else {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Bus Eklenemedi");
             }
        });

        VBox layout = new VBox(10,plakaLabel,idField,koltukLabel,busTypeField,totalSeatsLabel,totalSeatsField,saveButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        formStage.setScene(scene);
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.showAndWait();




    }

    //tüm otobüsleri listeler
    public  void handleListBuses(){
        List<Bus> busList = busManager.getAllBuses();

        if (busList == null || busList.isEmpty()) {
            showInfo("Hiç Otobüs Bulunamadı");
        }

        Stage busListStage = new Stage();
        busListStage.setTitle("Otobüs Listesi");

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f2f5, #e9eaf2);");

        for (Bus bus : busList) {
            VBox busCard = createBusCar(bus);
            layout.getChildren().add(busCard);
        }
        Scene scene = new Scene (new ScrollPane(layout));
        busListStage.setScene(scene);
        busListStage.show();
    }

    //Her bir otobüs için bir car oluşturur
    private VBox createBusCar(Bus bus) {
        VBox card = new VBox(7);
        card.setPadding(new javafx.geometry.Insets(12));
        card.setSpacing(5);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: #d2d2d2;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(60,60,100,0.08), 8,0,0,2);"
        );
        card.setMaxWidth(360);

        Label idLabel = new Label("ID: " + bus.getBusID());
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        idLabel.setTextFill(Color.web("#3b5998"));

        Label seatLabel = new Label("Total Seats: " + bus.getTotalSeats());
        seatLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        seatLabel.setTextFill(Color.web("#3b5998"));

        Label busTypeLabel = new Label("Bus Type: " + bus.getBusType());
        busTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        busTypeLabel.setTextFill(Color.web("#3b5998"));

        card.getChildren().addAll(idLabel, seatLabel, busTypeLabel);

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
                if (busManager.deleteBus(bus)){
                    System.out.println("silindiiiiiiiiiiii");
                } // TripManager'da bu metodu eklemelisin

                // İsteğe bağlı: "Sefer silindi" mesajı göster
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Otobüs Silindi");
                alert.setHeaderText(null);
                alert.setContentText("Otobüs başarıyla silindi.");
                alert.showAndWait();
            } else {
                // Yeterince sürüklenmediyse kart eski yerine dönsün
                card.setTranslateX(0);
            }
        });
        return card;

    }

    

}
