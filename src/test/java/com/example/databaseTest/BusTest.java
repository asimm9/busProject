package com.example.databaseTest;

import org.example.helper.dao.BusDAO;
import org.example.managers.BusManager;
import org.example.models.Bus;
import org.example.models.Seat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    @Test
    public void addBus() {
        Bus bus = new Bus();
        BusDAO dao = new BusDAO();

        bus.setBusType("vip");
        bus.setSeatLayout(new Seat[5][5]);
        bus.setBusID("4");
        bus.setTotalSeats(4);
        BusManager busManager = BusManager.getInstance();
        assertFalse(dao.insertBus(bus));
    }
}
