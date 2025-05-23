package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TripDAO {
    public boolean insertTrip(Trip trip) {
        String sql = "INSERT INTO trips(trip_id, origin, destination, departure_time, bus_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trip.getTripID());
            stmt.setString(2, trip.getOrigin());
            stmt.setString(3, trip.getDestination());
            stmt.setString(4, trip.getDepartureTime().toString());
            stmt.setInt(5, trip.getBus().getBusID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
