package org.example.managers;

import org.example.helper.dao.ReservationDAO;
import org.example.helper.observer.ReservationObserver;
import org.example.models.Reservation;


import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    private static ReservationManager instance;
    private List<ReservationObserver> observers = new ArrayList<>();
    private ReservationDAO reservationDAO = new ReservationDAO();

    private ReservationManager() {
        // constructor boş çünkü rezervasyonlar DAO'dan çekiliyor
    }

    public static ReservationManager getInstance() {
        if (instance == null) {
            instance = new ReservationManager();
        }
        return instance;
    }

    // Gözlemcilere bildirim metodları
    private void notifyReservationCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationAdded(reservation);
        }
    }

    private void notifyReservationCancelled(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationRemoved(reservation);
        }
    }


    //  Rezervasyon işlemleri (bildirimli)
    public boolean createReservation(Reservation reservation) {
        boolean success = reservationDAO.createReservation(reservation);
        if (success) {
            notifyReservationCreated(reservation);
        }
        return success;
    }

    public boolean cancelReservation(Reservation reservation) {
        boolean success = reservationDAO.cancelReservationById(reservation.getId());
        if (success) {
            notifyReservationCancelled(reservation);
        }
        return success;
    }

    public Reservation getReservationById(String userId) {
        return reservationDAO.getReservationById(userId);
    }

    public List<Reservation> getReservations(String userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }
}
