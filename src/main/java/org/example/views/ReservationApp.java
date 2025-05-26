package org.example.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.helper.DatabaseInitializer;
import org.example.managers.ReservationManager;
import org.example.models.*;
import org.example.helper.observer.ReservationLogger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReservationApp extends Application {

    private int reservationIdCounter = 1;
    private Reservation lastReservation;

    @Override
    public void start(Stage primaryStage) {
        ReservationManager manager = ReservationManager.getInstance();
        manager.registerObserver(new ReservationLogger());

        // Giriş alanları
        TextField usernameField = new TextField();
        usernameField.setPromptText("Kullanıcı adı");

       /* TextField fullNameField = new TextField();
        fullNameField.setPromptText("Ad Soyad");*/

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField fromField = new TextField();
        fromField.setPromptText("Nereden");

        TextField toField = new TextField();
        toField.setPromptText("Nereye");

        TextField seatField = new TextField();
        seatField.setPromptText("Koltuk No");

        Button createButton = new Button("Create Reservation");
        Button cancelButton = new Button("Cancel Last Reservation");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        Button listButton = new Button("List Reservations");

        // ID'ye göre iptal için
        TextField cancelIdField = new TextField();
        cancelIdField.setPromptText("İptal edilecek rezervasyon ID");


        outputArea.setEditable(false);





        createButton.setOnAction(e -> {

            try {
                // Girişleri al
                String username = usernameField.getText();
                //String fullName = fullNameField.getText();
                String email = emailField.getText();
                String from = fromField.getText();
                String to = toField.getText();
                int seatNumber = Integer.parseInt(seatField.getText());
                UserModel user = new UserModel();
                user.setUsername(username);
                user.setEmail(email);
                Trip trip = new Trip();
                trip.setOrigin(from);
                trip.setDestination(to);
                Seat seat = new Seat();
                seat.setSeatNumber(seatNumber);

                Reservation r = new Reservation();
                r.setId(UUID.randomUUID().toString());
                r.setUser(user);
                r.setTrip(trip);
                r.setSeat(seat);
                r.setReservationDateTime(LocalDateTime.now());

                boolean success = manager.createReservation(r);
                if (success) {
                    lastReservation = r;
                    outputArea.appendText("[✔] Rezervasyon oluşturuldu:\n" + r + "\n\n");
                } else {
                    outputArea.appendText("[✘] Oluşturulamadı.\n\n");
                }
            } catch (Exception ex) {
                outputArea.appendText("[!] Hatalı giriş! Tüm alanları doğru doldurun.\n\n");
            }
        });


        // ✅ Rezervasyon Listeleme (ID dahil)
        listButton.setOnAction(e -> {
            List<Reservation> reservations = manager.getReservations();
            if (reservations.isEmpty()) {
                outputArea.appendText("[ℹ] Hiç rezervasyon bulunamadı.\n\n");
            } else {
                outputArea.appendText("=== Rezervasyon Listesi ===\n");
                for (Reservation r : reservations) {
                    String username;
                    if (r.getUser() != null && r.getUser().getUsername() != null) {
                        username = r.getUser().getUsername();
                    } else {
                        username = "Bilinmeyen Kullanıcı";
                    }
                    int seatNo = (r.getSeat() != null) ? r.getSeat().getSeatNumber() : -1;

                    outputArea.appendText(
                            "ID: " + r.getId()
                                    + " | Kullanıcı: " + username
                                    + " | Koltuk: " + (seatNo >= 0 ? seatNo : "—")
                                    + "\n"
                    );
                }

                outputArea.appendText("\n");
            }
        });

        // ✅ ID'ye Göre Rezervasyon İptali
        cancelButton.setOnAction(e -> {
            try {
                String id = cancelIdField.getText();
                // Tüm rezervasyonlar içinde arayıp iptal
                Reservation target = null;
                for (Reservation r : manager.getReservations()) {
                    if (r.getId().equals(id)) {
                        target = r;
                        break;
                    }
                }
                if (target != null) {
                    boolean success = manager.cancelReservation(target);
                    if (success) {
                        outputArea.appendText("[✔] Rezervasyon iptal edildi: ID " + id + "\n\n");
                    } else {
                        outputArea.appendText("[✘] İptal başarısız.\n\n");
                    }
                } else {
                    outputArea.appendText("[!] Bu ID'ye ait rezervasyon bulunamadı.\n\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.appendText("[!] Lütfen geçerli bir ID girin.\n\n");
            }
        });

        VBox root = new VBox(10,
                new Label("Kullanıcı Bilgileri"), usernameField,  emailField,
                new Label("Yolculuk Bilgileri"), fromField, toField, seatField,
                createButton, listButton,
                new Label("Rezervasyon İptali (ID):"), cancelIdField, cancelButton,
                outputArea
        );
        root.setStyle("-fx-padding: 20");

        Scene scene = new Scene(root, 500, 650);
        primaryStage.setTitle("Reservation GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        launch(args);
    }
}

