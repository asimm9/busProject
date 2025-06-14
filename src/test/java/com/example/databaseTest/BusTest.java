package com.example.databaseTest;

import org.example.helper.dao.VehicleDAO;
import org.example.managers.VehicleManager;
import org.example.models.Bus;
import org.example.models.Seat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    @Test
    public void addBus() {
        Bus bus = new Bus();
        VehicleDAO dao = new VehicleDAO();

        bus.setVehicleType("vip");
        bus.setSeatLayout(new Seat[5][5]);
        bus.setVehicleID("4");
        bus.setTotalSeats(4);
        VehicleManager veichleManager = VehicleManager.getInstance();
        assertFalse(dao.insertBus(bus));
    }
}
