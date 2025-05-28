package org.example.controller;

import org.example.managers.SeatManager;
import org.example.models.Seat;
import org.example.views.SeatLayout;

import java.util.ArrayList;
import java.util.List;

public class SeatLayoutController {

    private SeatLayout view;
    private SeatManager manager;

    public SeatLayoutController(SeatLayout view) {
        this.view = view;
        this.manager = new SeatManager();
    }

    public void handleSelectSeat(){
        if (view.secilenKoltuklar.isEmpty()){
            System.out.println("Henüz Koltuk Seçmediniz");
        }else{
            List<Seat> seatList = view.selectedSeats;//List<Buttondan> List<Seat> e çevrildiğinde burası değişecek
            if(manager.insertSeatsByTrip(seatList)){
                //eğer ki koltuk seçme işlemi dbye başarıyla kaydedildiyse burası çalışaak
                System.out.println("koltuk seçme başarlııııııı");
            }else{
                //eğer ki başarasızsa buradan kullanıcıya uyarı mesajı verilecek
                System.out.println("başını aldınnnnn");
            }
        }
    }


}
