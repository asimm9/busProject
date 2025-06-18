package org.example.managers;

import org.example.helper.dao.SeatDAO;
import org.example.models.Seat;

import java.util.List;

public class SeatManager {
    public static SeatManager instance;
    SeatDAO seatDAO = new SeatDAO();

    //singelton nesne burda üretilir.
    public static SeatManager getInstance() {
        if (instance == null) {
            instance = new SeatManager();
        }
        return instance;
    }


    //koltuklar seçilirse veya iptal edilirse bu metodla güncellenir.
    public boolean insertSeatsByTrip(List<Seat> seats) {
        return seatDAO.updatesSeatByTrip(seats);
    }

    //otobüse göre koltuklar eklenir sefer ve koltuklara göre
    public boolean insertSeatByBus(Seat[][] seats) {
        return seatDAO.insertSeatByBusID(seats);
    }

    //sefer ve kullanıcıya göre seçilen koltuğu getirir.
    public List<Seat> getSeatByTripAndUserID(String tripID) {
        return seatDAO.getSeatByTripAndUserID(tripID);
    }

    public List<Seat> getSeatByTripandUserId(String tripID, String userID) {
        return seatDAO.getSeatByTripAndUserID(tripID, userID);
    }

    public boolean deleteByVehicleId(String vehicleID) {
        return seatDAO.deleteSeatsByBusId(vehicleID);
    }

}
