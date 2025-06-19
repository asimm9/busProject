package org.example.managers;

import org.example.helper.dao.ReservationDAO;
import org.example.models.Reservation;
import org.example.models.VehicleType;

import java.util.List;

public class ReservationManager {

    private static ReservationManager instance;
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






    //  Rezervasyon işlemleri (bildirimli)
    public boolean createReservation(Reservation reservation) {
        boolean success = reservationDAO.createReservation(reservation);
            return success;
    }

    public boolean cancelReservation(Reservation reservation) {
        boolean success = reservationDAO.cancelReservationById(reservation.getId());
               return success;
    }

    public Reservation getReservationById(String userId) {
        return reservationDAO.getReservationById(userId);
    }

    public List<Reservation> getReservations(String userId, VehicleType vehicleType) {
        return reservationDAO.getReservationsByUserId(userId,vehicleType);
    }
}
