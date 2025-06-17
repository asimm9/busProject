package com.example.databaseTest;

import org.example.helper.DatabaseInitializer;
import org.example.helper.dao.VehicleDAO;
import org.example.managers.VehicleManager;
import org.example.models.Bus;
import org.example.models.Seat;
<<<<<<< Updated upstream
import org.example.models.enums.VehicleType;
=======
import org.example.models.Vehicle;
import org.example.models.VehicleType;
>>>>>>> Stashed changes
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    @Test
    public void connectDB(){
        DatabaseInitializer.initialize();}


    @Test
    public void addBus() {
        Bus bus = new Bus();
        VehicleDAO dao = new VehicleDAO();

        bus.setVehicleType(VehicleType.Bus);
        bus.setSeatLayout(new Seat[5][5]);
<<<<<<< Updated upstream
        bus.setTotalSeats(4);
        VehicleManager veichleManager = VehicleManager.getInstance();
        assertFalse(dao.insertVehicle(bus));
=======
        bus.setId("4");
        bus.setTotalSeats(4);
        VehicleManager vehicleManager = VehicleManager.getInstance();
        assertTrue(dao.insertVehicle(bus));
    }
    @Test
    public void deleteVehicle() {
        Vehicle bus = new Bus();
        bus.setVehicleType(VehicleType.Bus);
        bus.setSeatLayout(new Seat[5][5]);
        bus.setId("4");
        bus.setTotalSeats(4);
        VehicleManager veichleManager = VehicleManager.getInstance();
        assertTrue(veichleManager.deleteVehicle(bus));
>>>>>>> Stashed changes
    }

    @Test
    public void getVeichleById() {
        VehicleManager veichleManager = VehicleManager.getInstance();
        assertNotNull(veichleManager.getVehicleById("4",VehicleType.Bus));
    }


    //Burası gerekli mi bilmiyom
    @Test
    public void getAllVehicles() {
        VehicleManager veichleManager = VehicleManager.getInstance();
        assertNotNull(veichleManager.getAllVehicles());
    }

}
