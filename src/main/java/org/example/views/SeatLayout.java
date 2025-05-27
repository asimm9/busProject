package org.example.views;

import javafx.application.Application;
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

import java.util.ArrayList;
import java.util.List;

public class SeatLayout extends Application {

    private GridPane grid;
    private List<Button> secilenKoltuklar = new ArrayList<>();
    private BorderPane root;
    private VBox centerBox;
    private ScrollPane scrollPane;

    @Override
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

        Button onayla = new Button("Onayla");
        onayla.setPrefWidth(200);
        onayla.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        onayla.setOnAction(e -> {
            for (Button b : secilenKoltuklar) {
                b.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");
                b.setDisable(true);
            }
            secilenKoltuklar.clear();
        });

        centerBox.getChildren().addAll(scrollPane, onayla);

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
        secilenKoltuklar.clear();

        grid = new GridPane();
        grid.setHgap(10); // Koltuklar arası mesafe azaltıldı
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int siraSayisi = 10;
        int koltukNumarasi = 1;

        grid.getChildren().clear();

        for (int i = 0; i < siraSayisi; i++) {
            if (ikiArtıBir) {
                for (int j = 0; j < 2; j++) {
                    Button button = createSeatButton(String.valueOf(koltukNumarasi++));
                    grid.add(button, j, i);
                }
                Button button = createSeatButton(String.valueOf(koltukNumarasi++));
                grid.add(button, 3, i);
            } else {
                for (int j = 0; j < 2; j++) {
                    Button button = createSeatButton(String.valueOf(koltukNumarasi++));
                    grid.add(button, j, i);
                }

                Region spacer = new Region();
                spacer.setPrefWidth(10); // Aradaki boşluk azaltıldı
                grid.add(spacer, 2, i);

                for (int j = 3; j < 5; j++) {
                    Button button = createSeatButton(String.valueOf(koltukNumarasi++));
                    grid.add(button, j, i);
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

        ayarlaSecimDavranisi(button);

        return button;
    }

    private void ayarlaSecimDavranisi(Button button) {
        button.setOnAction(e -> {
            if (!button.isDisabled()) {
                if (button.getStyle().contains("#00ff00")) {
                    button.setStyle("-fx-background-color: transparent;");
                    secilenKoltuklar.remove(button);
                } else {
                    button.setStyle("-fx-background-color:#00ff00;");
                    secilenKoltuklar.add(button);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
