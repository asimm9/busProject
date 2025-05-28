package org.example.managers;

import org.example.helper.dao.SeatDAO;
import org.example.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatManager {
    SeatDAO seatDAO = new SeatDAO();

    public boolean insertSeatsByTrip(List<Seat> seats) {
        return seatDAO.insertsSeatByTrip(seats);
    }

}
