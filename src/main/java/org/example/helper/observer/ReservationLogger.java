package org.example.helper.observer;


import org.example.models.Reservation;

public class ReservationLogger implements ReservationObserver {
    @Override
    public void onReservationAdded(Reservation reservation) {
        System.out.println("New reservation logged: " + reservation.toString());
    }
    @Override
    public void onReservationRemoved(Reservation reservation) {
        System.out.println("[LOG] Rezervasyon silindi: " + reservation.toString());
    }
}
