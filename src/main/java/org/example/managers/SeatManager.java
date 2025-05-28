package org.example.managers;

import org.example.helper.dao.SeatDAO;
import org.example.models.Seat;

import java.util.List;

public class SeatManager {
    public static SeatManager instance;

    public static SeatManager getInstance() {
        if (instance == null) {
            instance = new SeatManager();
        }
        return instance;
    }

    SeatDAO seatDAO = new SeatDAO();

    public boolean insertSeatsByTrip(List<Seat> seats) {
        return seatDAO.updatesSeatByTrip(seats);
    }

    public boolean insertSeatByBus(Seat[][] seats) {
        return seatDAO.insertSeatByBusID(seats);
    }
}
