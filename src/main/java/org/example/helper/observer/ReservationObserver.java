package org.example.helper.observer;

import org.example.models.Reservation;

public interface ReservationObserver {
    void onReservationAdded(Reservation reservation);
    void onReservationRemoved(Reservation reservation);

}
