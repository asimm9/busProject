package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.controller.SeatLayoutController;
import org.example.models.Seat;
import org.example.models.Trip;
import org.example.models.UserModel;
import org.example.models.interfaces.EconomyClass;
import org.example.models.interfaces.VipClass;

import java.util.ArrayList;
import java.util.List;

public class SeatLayout {

    public final SeatLayoutController controller;
    private GridPane grid;
    private BorderPane root;
    private ScrollPane scrollPane;
    private VBox centerBox;
    public final Trip trip;
    public final UserModel user;

    public List<Button> secilenKoltuklar;
    public List<Seat> selectedSeats;
    public List<Seat> reservedSeats;
    public Button confirmButton;
    public List<Seat> allSeats;

    public SeatLayout(Trip trip, UserModel user) {
        controller = new SeatLayoutController(this);
        secilenKoltuklar = new ArrayList<>();
        selectedSeats = new ArrayList<>();
        allSeats = new ArrayList<>();
        reservedSeats = controller.manager.getSeatByTripAndUserID(trip.getTripID()); // dışarıdan dolu koltukları al
        this.trip = trip;
        this.user = user;
    }

    public void start(Stage primaryStage) {
        root = new BorderPane();

        centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        olusturKoltukGrid();

        confirmButton = new Button("Onayla");
        confirmButton.setPrefWidth(200);
        confirmButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> controller.handleSelectSeat());

        centerBox.getChildren().addAll(scrollPane, confirmButton);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Otobüs Koltuk Seçimi");
        primaryStage.show();
    }

    private void olusturKoltukGrid() {
        boolean ikiArtıBir = "2+1".equals(trip.getVehicle().getSeatType());

        secilenKoltuklar.clear();
        selectedSeats.clear();
        allSeats.clear();

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int siraSayisi = switch (trip.getVehicle().getTotalSeats()) {
            case 48 -> ikiArtıBir ? 16 : 12;
            case 36 -> ikiArtıBir ? 12 : 9;
            default -> throw new Error("Geçersiz koltuk sayısı");
        };

        int koltukNumarasi = 1;

        for (int i = 0; i < siraSayisi; i++) {
            if (ikiArtıBir) {
                for (int j = 0; j < 2; j++) {
                    boolean reserved = isSeatReserved(String.valueOf(koltukNumarasi));
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi), i + 1, j + 1, reserved);
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);
                    koltukNumarasi++;
                }
                boolean reserved = isSeatReserved(String.valueOf(koltukNumarasi));
                Seat seat = createSeatInstance(String.valueOf(koltukNumarasi), i + 1, 3, reserved);
                allSeats.add(seat);
                Button button = createSeatButton(String.valueOf(koltukNumarasi));
                grid.add(button, 3, i);
                koltukNumarasi++;
            } else {
                for (int j = 0; j < 2; j++) {
                    boolean reserved = isSeatReserved(String.valueOf(koltukNumarasi));
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi), i + 1, j + 1, reserved);
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);
                    koltukNumarasi++;
                }

                Region spacer = new Region();
                spacer.setPrefWidth(10);
                grid.add(spacer, 2, i);

                for (int j = 3; j < 5; j++) {
                    boolean reserved = isSeatReserved(String.valueOf(koltukNumarasi));
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi), i + 1, j, reserved);
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);
                    koltukNumarasi++;
                }
            }
        }

        scrollPane.setContent(grid);
    }

    private boolean isSeatReserved(String seatID) {
        for (Seat seat : reservedSeats) {
            if (seat.getSeatID().equals(seatID)) {
                return true;
            }
        }
        return false;
    }

    private Button createSeatButton(String number) {
        Button button = new Button();
        button.setPrefSize(80, 60);

        Image image = new Image(getClass().getResource("/images/boss.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(45);
        imageView.setPreserveRatio(true);

        Label label = new Label(number);
        label.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");

        StackPane content = new StackPane(imageView, label);
        button.setGraphic(content);

        int index = Integer.parseInt(number) - 1;
        Seat seat = allSeats.get(index);

        if (seat.isReserved()) {
            button.setStyle("-fx-background-color: red;");
            button.setDisable(true);
        } else {
            if (seat.getSeatClass().getClassName().equals("VIP")){
                button.setStyle("-fx-background-color: yellow;");
            }else if (seat.getSeatClass().getClassName().equals("Economy")){
                button.setStyle("-fx-background-color: blue;");
            }
            ayarlaSecimDavranisi(button, seat);
        }

        return button;
    }

    private Seat createSeatInstance(String seatID, int row, int column, boolean isReserved) {
        Seat seat = new Seat();
        seat.setTripID(trip.getTripID());
        seat.setVehicleID(trip.getVehicle().getId());
        seat.setUserID(user.getId());
        seat.setSeatID(seatID);
        seat.setRow(row);
        seat.setColumn(column);
        seat.setReserved(isReserved);
        if (row<5){
            seat.setSeatClass(new VipClass());
        }else {
            seat.setSeatClass(new EconomyClass());
        }
        return seat;
    }

    private void ayarlaSecimDavranisi(Button button, Seat seat) {
        button.setOnAction(e -> {
            if (!button.isDisabled()) {
                if (button.getStyle().contains("#00ff00")) {
                    button.setStyle("-fx-background-color: transparent;");
                    secilenKoltuklar.remove(button);
                    seat.setReserved(false);
                    selectedSeats.remove(seat);
                } else {
                    button.setStyle("-fx-background-color:#00ff00;");
                    secilenKoltuklar.add(button);
                    seat.setReserved(true);
                    selectedSeats.add(seat);
                }
            }
        });
    }
}
