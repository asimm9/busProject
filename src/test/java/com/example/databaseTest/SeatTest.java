package com.example.databaseTest;

import org.example.helper.DatabaseInitializer;
import org.example.managers.SeatManager;
import org.example.models.Seat;
import org.example.models.interfaces.EconomyClass;
import org.example.models.interfaces.SeatClassStrategy;
import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SeatTest {

    private static SeatManager seatManager;

    @BeforeAll
    static void setup() {
        seatManager = SeatManager.getInstance();
        DatabaseInitializer.initialize();
    }
    @Test
    public void insertSeatsByTripTest() {

        List<Seat> seatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setReserved(false);
        seat.setSeatClass(new EconomyClass());
        seat.setTripID("test trip");
        seat.setUserID("test User");
        seat.setRow(1);
        seat.setColumn(1);
        seat.setVehicleID("test Vehicle");
        seat.setSeatID("test Seat");

        seatList.add(seat);

        assertTrue(seatManager.insertSeatsByTrip(seatList));

    }

    @Test
    public void insertSeatByBus() {

        Seat[][] seats = new Seat[1][1];
        seats[0][0] = new Seat();
        seats[0][0].setReserved(true);
        seats[0][0].setSeatClass(new EconomyClass());
        seats[0][0].setTripID("test trip");
        seats[0][0].setUserID("test User");
        seats[0][0].setRow(1);
        seats[0][0].setColumn(1);
        seats[0][0].setVehicleID("test Vehicle");
        seats[0][0].setSeatID("test Seat");

        assertTrue(seatManager.insertSeatByBus(seats));
    }

    @Test
    public void getSeatByTripAndUserID() {

        List<Seat> expectedSeatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setReserved(false);
        seat.setSeatClass(new EconomyClass());
        seat.setTripID("test trip");
        seat.setUserID("test User");
        seat.setRow(1);
        seat.setColumn(1);
        seat.setVehicleID("test Vehicle");
        seat.setSeatID("test Seat");
        expectedSeatList.add(seat);

        List<Seat> actualSeats = seatManager.getSeatByTripAndUserID("test trip");

        assertNotNull(actualSeats);
        assertEquals(expectedSeatList.size(), actualSeats.size());
    }

    @Test
    public void getSeatByTripandUserId() {

        List<Seat> expectedSeatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setReserved(false);
        seat.setSeatClass(new EconomyClass());
        seat.setTripID("test trip");
        seat.setUserID("test User");
        seat.setRow(1);
        seat.setColumn(1);
        seat.setVehicleID("test Vehicle");
        seat.setSeatID("test Seat");
        expectedSeatList.add(seat);


        List<Seat> actualSeats = seatManager.getSeatByTripandUserId("test trip", "test User");

        assertNotNull(actualSeats);
        assertEquals(expectedSeatList.size(), actualSeats.size());

    }

    @Test
    public void deleteByVehicleId() {

        assertTrue(seatManager.deleteByVehicleId("test Vehicle"));
    }






    
}
