package org.example.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.managers.VehicleManager;
import org.example.managers.SeatManager;
import org.example.managers.TripManager;
import org.example.models.*;

import org.example.models.factory.VehicleFactory;
import org.example.models.interfaces.EconomyClass;
import org.example.models.interfaces.VipClass;
import org.example.views.AdminPanel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class AdminPanelController {

    private final AdminPanel view;
    private UserModel user;
    Label plakaLabel;
    private TripManager tripManager = TripManager.getInstance();
    private VehicleManager vehicleManager = VehicleManager.getInstance();
    private SeatManager seatManager = SeatManager.getInstance();

    //viewa bağlanma kısmı controller constructerında yapılıyor.
    public AdminPanelController(AdminPanel view,UserModel user) {
        this.view = view; this.user = user;
    }

    //Otobüs sekmesinde seçili olduğumuzu gösteriyor
    public void handleBusSelected() {
        view.getBusCard().setVisible(true);
        view.getPlaneCard().setVisible(false);
        view.setBus(true);
        view.getBusButton().setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.getPlaneButton().setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
    }

    //Uçak sekmesinde seçili olduğunu gösteriyor
    public void handlePlaneSelected() {
        view.getBusCard().setVisible(false);
        view.getPlaneCard().setVisible(true);
        view.setBus(false);
        view.getPlaneButton().setStyle("-fx-background-radius: 10; -fx-background-color: #ffffff; -fx-text-fill: #8b0033;");
        view.getBusButton().setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-text-fill: #555555;");
    }

    //adminin yeni bir trip ekleme işi burdan yapılıyor
    public void handleAddTrip() {
        String origin = view.getFromField().getText();
        String destination = view.getToField().getText();
        String now = view.getTimeField().getText();

        String busID = view.getBusCard().isVisible() ? view.getBusIdField().getText() : view.getPlaneIdField().getText();
        LocalDate selected = view.getDatePicker().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatted = selected.format(formatter);


        if (!origin.isEmpty() && !destination.isEmpty() /*&& !date.isEmpty() && !now.isEmpty()*/ && !busID.isEmpty()) {


            Trip trip = new Trip();
            VehicleType vehicleType;
            if (view.isBus()){
                vehicleType = VehicleType.Bus;
            }else{
                vehicleType = VehicleType.Plane;
            }
            Vehicle vehicle = vehicleManager.getVehicleById(busID, vehicleType);


            trip.setTripID(UUID.randomUUID().toString());
            trip.setOrigin(origin);
            trip.setDestination(destination);
            trip.setDepartureTime(formatted);
            trip.setTime(now);
            trip.setVehicle(vehicle);


            if (tripManager.getTripByBusId(vehicle.getId(),vehicleType) == null){
                if (tripManager.createTrip(trip)){
                    view.getMessageLabel().setText("Sefer Eklendi");
                }else {
                    view.getMessageLabel().setText("Sefer Oluşturulamadı");
                }
            }else {
                view.getMessageLabel().setText("Aynı otobüs başka bir sefere eklendi");
            }

        } else {
            view.getMessageLabel().setText("Lütfen tüm alanları doldurun!");


        }
    }

    //tüm tripleri listelediğimiz metod
    public void handleListTrips() {
        VehicleType vehicleType;
        vehicleType = view.isBus() == true ? VehicleType.Bus : VehicleType.Plane;
        List<Trip> tripList = tripManager.getAllTrips(vehicleType);

        if (tripList == null || tripList.isEmpty()) {
            showInfo("Hiç sefer bulunamadı.");
            return;
        }

        // Pencere başlat
        Stage listStage = new Stage();
        listStage.setTitle("Tüm Seferler");

        VBox cardsBox = new VBox(15);
        cardsBox.setPadding(new Insets(20));
        cardsBox.setAlignment(Pos.TOP_CENTER);

        for (Trip trip : tripList) {
            if (trip.getVehicle() == null) {
                continue;
            }

            VBox card = createTripCard(trip);
            cardsBox.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(cardsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox root = new VBox(scrollPane);
        root.setPadding(new Insets(15));

        // Gradient arka plan
        Background gradient = new Background(
                new BackgroundFill(
                        new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#3b5998")),
                                new Stop(1, Color.web("#8b0033"))
                        ),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        );
        root.setBackground(gradient);

        Scene scene = new Scene(root, 450, 480);
        listStage.setScene(scene);
        listStage.show();
    }

    //cardın ui ı burda olur ve silinme işlemi de sola sürüklemeli oluyo metod burda işleniyor
    private VBox createTripCard(Trip trip) {
        VBox card = new VBox(7);
        card.setPadding(new Insets(12));
        card.setSpacing(5);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-color: #d2d2d2;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );
        card.setMaxWidth(360);

        Label fromTo = new Label("Kalkış: " + trip.getOrigin() + "   ➔   Varış: " + trip.getDestination());
        fromTo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        fromTo.setTextFill(Color.web("#222222"));

        Label dateLabel = new Label("Tarih: " + trip.getDepartureTime());
        dateLabel.setFont(Font.font("Arial", 13));
        dateLabel.setTextFill(Color.web("#444444"));



        Label timeLabel = new Label("Saat: " + trip.getTime());
        timeLabel.setFont(Font.font("Arial", 13));
        timeLabel.setTextFill(Color.web("#444444"));

        Vehicle transportInfo = trip.getVehicle();
        System.out.println(transportInfo.toString());
        Label transportLabel = new Label(
                transportInfo.getVehicleType().getName() + " tipi: " + transportInfo.getSeatType()
        );
        transportLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        transportLabel.setTextFill(Color.web("#8b0033"));

        // 🔧 Bu satır kritik: içerikleri karta doğru şekilde ekle
        card.getChildren().addAll(fromTo, dateLabel, timeLabel, transportLabel);

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tüm Seferler");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    // Bus eklemek için bu metod kullanılıyor
    public void handleInsertVeihcle() {
        Stage formStage = new Stage();
        if (view.isBus()) {
            formStage.setTitle("Otobüs Ekle");
            plakaLabel = new Label("Plaka:");
        } else {
            formStage.setTitle("Uçak Ekle");
            plakaLabel = new Label("Uçak ID:");
        }

        // Form bileşenleri

        TextField idField = new TextField();

        Label koltukLabel = new Label("Koltuk Tipi:");
        ComboBox<String> seatTypeComboBox = new ComboBox<>();
        seatTypeComboBox.getItems().addAll("2+2", "2+1");
        seatTypeComboBox.setPromptText("Koltuk tipi seçin");

        Label totalSeatsLabel = new Label("Toplam Koltuk:");
        ComboBox<String> totalSeatsComboBox = new ComboBox<>();
        totalSeatsComboBox.getItems().addAll("36", "48");
        totalSeatsComboBox.setPromptText("Koltuk sayısı seçin");

        Label priceLabel = new Label("Fiyat:");
        TextField priceField = new TextField();


        Button saveButton = new Button("Kaydet");
        saveButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        saveButton.setTextFill(Color.web("#8b0033"));
        saveButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        saveButton.setPadding(new Insets(10, 20, 10, 20));

        saveButton.setOnAction(event -> {
            String id = idField.getText();
            String seatType = seatTypeComboBox.getValue(); // Text yerine ComboBox'tan alınır
            String totalSeats = totalSeatsComboBox.getValue();
            String price = priceField.getText();

            Vehicle vehicle;
            VehicleType vehicleType;
            if (view.isBus()) {
                vehicle = VehicleFactory.createVehicle(VehicleType.Bus);
                vehicleType = VehicleType.Bus;
            } else {
                vehicle = VehicleFactory.createVehicle(VehicleType.Plane);
                vehicleType = VehicleType.Plane;
            }
            vehicle.setSeatType(seatType);
            vehicle.setId(id);
            vehicle.setTotalSeats(Integer.parseInt(totalSeats));
            vehicle.setVehicleType(vehicleType);
            vehicle.setPrice(Integer.parseInt(price));
            if (vehicle instanceof Plane) {
                List<Integer> numbers = Arrays.asList(23, 28, 35, 48); // 4 integer
                Random random = new Random();


                int randomIndex = random.nextInt(numbers.size());
                int selectedNumber = numbers.get(randomIndex);
                ((Plane) vehicle).setBaggage(selectedNumber);
            }


            Seat[][] seats;
            int seatNumber = 1;
            int total = Integer.parseInt(totalSeats);

            if (total == 36) {
                if (seatType.equals("2+2")) {
                    seats = new Seat[9][4];
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 4; j++) {
                            Seat seat = new Seat();
                            seat.setColumn(j);
                            seat.setRow(i);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber++));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setVehicleID(id);
                            if (i<3){
                                seat.setSeatClass(new VipClass());
                            }else{
                                seat.setSeatClass(new EconomyClass());
                            }
                            seats[i][j] = seat;
                        }
                    }
                } else {
                    seats = new Seat[12][3];
                    for (int i = 0; i < 12; i++) {
                        for (int j = 0; j < 3; j++) {
                            Seat seat = new Seat();
                            seat.setColumn(j);
                            seat.setRow(i);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber++));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setVehicleID(id);
                            if (i<3){
                                seat.setSeatClass(new VipClass());
                            }else{
                                seat.setSeatClass(new EconomyClass());
                            }
                            seats[i][j] = seat;
                        }
                    }
                }
            } else {
                if (seatType.equals("2+2")) {
                    seats = new Seat[12][4];
                    for (int i = 0; i < 12; i++) {
                        for (int j = 0; j < 4; j++) {
                            Seat seat = new Seat();
                            seat.setColumn(j);
                            seat.setRow(i);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber++));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setVehicleID(id);
                            if (i<3){
                                seat.setSeatClass(new VipClass());
                            }else{
                                seat.setSeatClass(new EconomyClass());
                            }
                            seats[i][j] = seat;
                        }
                    }
                } else {
                    seats = new Seat[16][3];
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 3; j++) {
                            Seat seat = new Seat();
                            seat.setColumn(j);
                            seat.setRow(i);
                            seat.setReserved(false);
                            seat.setSeatID(String.valueOf(seatNumber++));
                            seat.setUserID(null);
                            seat.setTripID(null);
                            seat.setVehicleID(id);
                            if (i<3){
                                seat.setSeatClass(new VipClass());
                            }else{
                                seat.setSeatClass(new EconomyClass());
                            }
                            seats[i][j] = seat;
                        }
                    }
                }
            }

            seatManager.insertSeatByBus(seats);
            vehicle.setSeatLayout(new Seat[3][3]); // varsayılan
            if (vehicleManager.insertVehicle(vehicle)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText(null);
                alert.setContentText("Araç başarıyla eklendi.");
                alert.showAndWait();
                formStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Araç eklenemedi!");
                alert.showAndWait();
            }
        });

        // Her alan için stil uygulanmış kutular
        VBox plakaBox = styleAsCard(plakaLabel, idField);
        VBox koltukBox = styleAsCard(koltukLabel, seatTypeComboBox);
        VBox koltukSayiBox = styleAsCard(totalSeatsLabel, totalSeatsComboBox);
        VBox priceBox = styleAsCard(priceLabel,priceField);


        VBox formLayout = new VBox(12, plakaBox, koltukBox, koltukSayiBox, priceBox, saveButton);
        formLayout.setPadding(new Insets(30));
        formLayout.setAlignment(Pos.CENTER);

        // Arka plan (gradient)
        Background gradientBackground = new Background(
                new BackgroundFill(
                        new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#3b5998")),
                                new Stop(1, Color.web("#8b0033"))),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        );
        formLayout.setBackground(gradientBackground);

        Scene scene = new Scene(formLayout, 420, 400);
        formStage.setScene(scene);
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.showAndWait();
    }

    //tüm araçları türüne göre listeler
    public void handleListBuses() {
        List<Vehicle> vehicleList = vehicleManager.getAllVehicles();
        VBox cardContainer = new VBox(20); // Kartlar arası boşluk artırıldı
        cardContainer.setPadding(new Insets(20));
        cardContainer.setAlignment(Pos.TOP_CENTER);

        for (Vehicle vehicle : vehicleList) {
            if(view.isBus()){
                VBox card = createVehicleCard(vehicle);
                if(vehicle.getVehicleType()==VehicleType.Bus){
                cardContainer.getChildren().add(card);
            }}
            else{
                VBox card = createVehicleCard(vehicle);
                if(vehicle.getVehicleType()== VehicleType.Plane){
                    cardContainer.getChildren().add(card);
                }

            }


        }

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");


        VBox root = new VBox(scrollPane);

        // Arka planı gradient yap
        Background gradient = new Background(
                new BackgroundFill(
                        new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#3b5998")),
                                new Stop(1, Color.web("#8b0033"))
                        ),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        );
        root.setBackground(gradient);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 500);

        if (view.isBus()) {
            if (vehicleList == null || vehicleList.isEmpty()) {

                showInfo("Hiç Otobüs Bulunamadı");
                return;
            }
                Stage busListStage = new Stage();
                busListStage.setTitle("Otobüs Listesi");
                busListStage.setScene(scene);
                busListStage.show();

        }else {
            if (vehicleList == null || vehicleList.isEmpty()) {
                showInfo("Hiç Uçak bulunamadı");
                return;
            }
            Stage planeListStage = new Stage();
            planeListStage.setTitle("Uçak Listesi");
            planeListStage.setScene(scene);
            planeListStage.show();

        }



    }

    public void handleLogout(){
         user = null;
        Stage currentStage = (Stage) view.logoutButton.getScene().getWindow();
        currentStage.close();
    }

    //Her bir otobüs için bir car oluşturur
    private VBox createVehicleCard(Vehicle  vehicle) {
        VBox card = new VBox(7);
        card.setPadding(new Insets(12));
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
        Label idLabel = null;
        if(view.isBus()) {
            idLabel = new Label("Plaka: " + vehicle.getId());
        }else{
            idLabel = new Label("ID: " + vehicle.getId());
        }
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        idLabel.setTextFill(Color.web("#3b5998"));

        Label seatLabel = new Label("Total Seats: " + vehicle.getTotalSeats());
        seatLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        seatLabel.setTextFill(Color.web("#3b5998"));
        Label vehicleTypeLabel = null;
        if(view.isBus()) {
            vehicleTypeLabel = new Label("Bus Type: " + vehicle.getSeatType());
        }else{
            vehicleTypeLabel = new Label("Plane Type: " + vehicle.getSeatType());

        }
        vehicleTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        vehicleTypeLabel.setTextFill(Color.web("#3b5998"));

        card.getChildren().addAll(idLabel, seatLabel, vehicleTypeLabel);

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

                Trip toDeletedTrip = tripManager.getTripByBusId(vehicle.getId(),vehicle.getVehicleType());

                // Sonra veri kaynağından sil
                if (vehicleManager.deleteVehicle(vehicle) && seatManager.deleteByVehicleId(vehicle.getId()) && tripManager.deleteTrip(toDeletedTrip)) {
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



    private VBox styleAsCard(Label label, Node input) {
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#3b5998"));

        if (input instanceof TextField) {
            ((TextField) input).setPromptText("Bilgi giriniz");
            ((TextField) input).setMaxWidth(Double.MAX_VALUE);
        } else if (input instanceof ComboBox) {
            ((ComboBox<?>) input).setMaxWidth(Double.MAX_VALUE);
        }

        VBox box = new VBox(6, label, input);
        box.setPadding(new Insets(10));
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));
        box.setMaxWidth(300);
        return box;
    }



}
