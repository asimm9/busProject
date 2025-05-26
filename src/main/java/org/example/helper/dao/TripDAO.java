package org.example.helper.dao;

import javafx.scene.control.DatePicker;
import org.example.helper.DatabaseConnector;
import org.example.models.Bus;
import org.example.models.Seat;
import org.example.models.Trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    public boolean insertTrip(Trip trip) {
        String sql = "INSERT INTO trips(trip_id, origin, destination, departure_time, time, bus_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trip.getTripID());
            stmt.setString(2, trip.getOrigin());
            stmt.setString(3, trip.getDestination());
            stmt.setString(4, trip.getDepartureTime().toString());
            stmt.setString(5, trip.getTime().toString());
            stmt.setInt(6, trip.getBus().getBusID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTrip(Trip trip) {
        String sql = "DELETE FROM trips WHERE trip_id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trip.getTripID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Trip> getAllTrips() {
        String sql = "SELECT * FROM trips";
        List<Trip> trips = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql) ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                String tripID = rs.getString("trip_id");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");

                DatePicker departureTime = new DatePicker();
                int busId = rs.getInt("bus_id");

                Trip trip = new Trip();
                trip.setTripID(tripID);
                trip.setOrigin(origin);
                trip.setDestination(destination);
                trip.setDepartureTime(departureTime);
                Bus bus =new Bus();
                trip.setBus(bus);
                trips.add(trip);
            }
            return trips;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Trip getTrip(String tripID) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
            Trip trip = null;
            try (Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql) ) {
                stmt.setString(1, tripID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    trip = new Trip();
                    trip.setTripID(tripID);
                    trip.setOrigin(rs.getString("origin"));
                    trip.setDestination(rs.getString("destination"));
                    DatePicker departureTime = new DatePicker();
                    trip.setDepartureTime(departureTime);
                    trip.setTime(LocalDateTime.parse(rs.getString("time")));
                    int busId = rs.getInt("bus_id");
                    Bus bus = new Bus();
                    trip.setBus(bus);
                }
                return trip;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }
}
