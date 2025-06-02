package com.example.databaseTest;

import org.example.helper.dao.VeichleDAO;
import org.example.managers.VeichleManager;
import org.example.models.Bus;
import org.example.models.Seat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    @Test
    public void addBus() {
        Bus bus = new Bus();
        VeichleDAO dao = new VeichleDAO();

        bus.setBusType("vip");
        bus.setSeatLayout(new Seat[5][5]);
        bus.setBusID("4");
        bus.setTotalSeats(4);
        VeichleManager veichleManager = VeichleManager.getInstance();
        assertFalse(dao.insertBus(bus));
    }
}
