package org.example.controller;

import javafx.stage.Stage;
import org.example.helper.AppContext;
import org.example.managers.SeatManager;
import org.example.models.Seat;
import org.example.views.SeatLayout;

import java.util.ArrayList;
import java.util.List;

public class SeatLayoutController {

    private SeatLayout view;
    public SeatManager manager;

    public SeatLayoutController(SeatLayout view) {
        this.view = view;
        this.manager = new SeatManager();
    }

    public void handleSelectSeat(){
        if (view.secilenKoltuklar.isEmpty()){
            System.out.println("Henüz Koltuk Seçmediniz");
        }else{
            List<Seat> seatList = view.selectedSeats;//List<Buttondan> List<Seat> e çevrildiğinde burası değişecek

                //eğer ki koltuk seçme işlemi dbye başarıyla kaydedildiyse burası çalışaak
                Stage currentStage  = (Stage) view.confirmButton.getScene().getWindow();
                currentStage.close();
                AppContext.selectedSeats = seatList;
                System.out.println("koltuk seçme başarlııııııı");

            }
        }
    }

