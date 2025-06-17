package com.example.databaseTest;

import org.example.managers.SeatManager;
import org.example.models.Seat;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeatTest {

    private static SeatManager seatManager;

    @BeforeAll
    static void setup() {
        seatManager = SeatManager.getInstance();
        // Burada istersen test DB'yi başlatabilir veya mock nesneler kullanılabilir
    }

    @Test
    @Order(1)
    void testSeatBuilderAndGetters() {
        Seat seat = new Seat.Builder()
                .seatID("S1")
                .vehicleID("V1")
                .tripID("T1")
                .userID("U1")
                .row(3)
                .column(4)
                .build();
        seat.setReserved(true);

        assertEquals("S1", seat.getSeatID());
        assertEquals("V1", seat.getVehicleID());
        assertEquals("T1", seat.getTripID());
        assertEquals("U1", seat.getUserID());
        assertEquals(3, seat.getRow());
        assertEquals(4, seat.getColumn());
        assertTrue(seat.isReserved());
    }

    @Test
    @Order(2)
    void testSeatEqualsAndHashCode() {
        Seat seat1 = new Seat.Builder()
                .seatID("S2")
                .vehicleID("V1")
                .tripID("T2")
                .userID("U2")
                .row(1)
                .column(1)
                .build();
        seat1.setReserved(false);

        Seat seat2 = new Seat.Builder()
                .seatID("S2")
                .vehicleID("V1")
                .tripID("T2")
                .userID("U2")
                .row(1)
                .column(1)
                .build();
        seat2.setReserved(false);

        assertEquals(seat1, seat2);
        assertEquals(seat1.hashCode(), seat2.hashCode());

        seat2.setReserved(true);
        assertNotEquals(seat1, seat2);
    }



    
}
