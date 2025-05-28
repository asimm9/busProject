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

import java.util.ArrayList;
import java.util.List;

public class SeatLayout {

    private final SeatLayoutController controller;
    private GridPane grid;
    private BorderPane root;
    private ScrollPane scrollPane;
    private VBox centerBox;
    private final Trip trip;
    private final UserModel user;

    public List<Button> secilenKoltuklar;
    public List<Seat> selectedSeats;
    public Button confirmButton;
    public List<Seat> allSeats;


    public SeatLayout(Trip trip, UserModel user) {
        controller = new SeatLayoutController(this);
        secilenKoltuklar = new ArrayList<>();
        selectedSeats = new ArrayList<>();
        allSeats = new ArrayList<>();
        this.trip = trip;
        this.user = user;
    }

    public void start(Stage primaryStage) {
        root = new BorderPane();

        HBox secimBox = new HBox(20);
        secimBox.setPadding(new Insets(10));
        secimBox.setAlignment(Pos.CENTER);

        Button btn2plus1 = new Button("2+1 Otobüs Düzeni");
        Button btn2plus2 = new Button("2+2 Otobüs Düzeni");

        btn2plus1.setPrefWidth(150);
        btn2plus2.setPrefWidth(150);

        secimBox.getChildren().addAll(btn2plus1, btn2plus2);

        centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        olusturKoltukGrid(true); // ilk gösterim 2+1

        confirmButton = new Button("Onayla");
        confirmButton.setPrefWidth(200);
        confirmButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> controller.handleSelectSeat());


        centerBox.getChildren().addAll(scrollPane, confirmButton);

        root.setTop(secimBox);
        root.setCenter(centerBox);

        btn2plus1.setOnAction(e -> olusturKoltukGrid(true));
        btn2plus2.setOnAction(e -> olusturKoltukGrid(false));

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Otobüs Koltuk Seçimi");
        primaryStage.show();
    }

    private void olusturKoltukGrid(boolean ikiArtıBir) {

        //hem buton listesi hem de koltuk listeri sıfırlandı
        secilenKoltuklar.clear();
        selectedSeats.clear();
        allSeats.clear();

        //grid pane in sizeları ve konumu burda ayarlandı
        grid = new GridPane();
        grid.setHgap(10); // Koltuklar arası mesafe azaltıldı
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);



        int siraSayisi = 10; //row sayısı
        int koltukNumarasi = 1; // koltuk numarasını 1den başlatmak için

        grid.getChildren().clear();

        for (int i = 0; i < siraSayisi; i++) {
            if (ikiArtıBir) {
                for (int j = 0; j < 2; j++) {
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi),i+1,j+1);
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);
                    koltukNumarasi++;
                }
                Seat seat = createSeatInstance(String.valueOf(koltukNumarasi),i+1,3);
                allSeats.add(seat);
                Button button = createSeatButton(String.valueOf(koltukNumarasi));
                grid.add(button, 3, i);
                koltukNumarasi++;
            } else {
                for (int j = 0; j < 2; j++) {
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi),i+ 1 ,j+ 1 );
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);

                    koltukNumarasi++;
                }

                Region spacer = new Region();
                spacer.setPrefWidth(10); // Aradaki boşluk azaltıldı
                grid.add(spacer, 2, i);

                for (int j = 3; j < 5; j++) {
                    Seat seat = createSeatInstance(String.valueOf(koltukNumarasi),i,j);
                    allSeats.add(seat);
                    Button button = createSeatButton(String.valueOf(koltukNumarasi));
                    grid.add(button, j, i);

                    koltukNumarasi++;
                }
            }
        }

        scrollPane.setContent(grid); // Scroll içeriği güncelle
    }

    private Button createSeatButton(String number) {
        Button button = new Button();
        button.setPrefSize(80, 60); // Büyük koltuk boyutu

        Image image = new Image(getClass().getResource("/images/boss.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(45);
        imageView.setPreserveRatio(true);

        Label label = new Label(number);
        label.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");

        StackPane content = new StackPane(imageView, label);
        button.setGraphic(content);
        button.setStyle("-fx-background-color: transparent;");


        ayarlaSecimDavranisi(button,allSeats.get(Integer.parseInt(number)-1));

        return button;
    }
    private Seat createSeatInstance(String seatID,int row, int column) {  //seatID koltuk numarasına referans eder.
        Seat seat = new Seat();
        seat.setTripID(trip.getTripID());
        seat.setBusID(trip.getBus().getBusID());
        seat.setUserID(user.getId());
        seat.setSeatID(seatID);
        seat.setReserved(false);
        seat.setRow(row);
        seat.setColumn(column);
        return seat;
    }

    private void ayarlaSecimDavranisi(Button button,Seat seat) {
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
