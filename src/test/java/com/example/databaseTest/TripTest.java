package com.example.databaseTest;

import org.example.helper.DatabaseInitializer;
import org.example.managers.TripManager;
import org.example.models.Bus;
import org.example.models.Trip;
import org.example.models.Vehicle;
import org.example.models.VehicleType;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TripTest {

    private static TripManager tripManager;
    private static Trip testTrip;
    private static Vehicle testVehicle;

    @BeforeAll
    public static void setup() {
        // Veritabanı tablolarını oluştur
        DatabaseInitializer.initialize();

        tripManager = TripManager.getInstance();

        // Test için kullanılacak araç (vehicle) oluşturuluyor
        testVehicle = new Bus();
        testVehicle.setId("vehicle-test-id");  // Bu ID veritabanında var olmalı veya testte eklenmeli
        testVehicle.setVehicleType(VehicleType.Bus);

        // Burada Vehicle'ı veritabanına eklemelisin yoksa Trip oluşturulamaz.
        // Eğer TripDAO/VehicleDAO da yoksa, basit bir insert sorgusu yazabilirsin.
        // Örneğin manuel insert için aşağıdaki gibi basit örnek kod ekleyebilirsin (DAO yoksa):

        /*
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT OR IGNORE INTO vehicles(vehicle_id, seat_type, total_seats, type) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, testVehicle.getId());
            ps.setString(2, "standard");       // örnek seat_type
            ps.setInt(3, 40);                  // örnek total_seats
            ps.setString(4, testVehicle.getVehicleType().name());  // type sütunu
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Vehicle veritabanına eklenemedi.");
        }
        */

        // Test için Trip nesnesi oluşturuluyor
        testTrip = new Trip();
        testTrip.setTripID(UUID.randomUUID().toString());
        testTrip.setOrigin("Istanbul");
        testTrip.setDestination("Ankara");
        testTrip.setDepartureTime("10:00");
        testTrip.setTime(LocalDateTime.now().plusDays(1));
        testTrip.setVehicle(testVehicle);
    }

    @Test
    @Order(1)
    public void testCreateTrip() {
        boolean result = tripManager.createTrip(testTrip);
        assertTrue(result, "Trip başarıyla eklenmeli.");
    }


    @Test
    @Order(3)
    public void testGetAllTrips() {
        List<Trip> trips = tripManager.getAllTrips();
        assertNotNull(trips, "Trip listesi boş olmamalı.");
        assertTrue(trips.size() > 0, "En az bir trip olmalı.");
    }
}

