package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationDAO {
    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations(user_id, trip_id, seat_id, reservation_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reservation.getUser().getId());
            stmt.setString(2, reservation.getTrip().getTripID());
            stmt.setInt(3, reservation.getSeat().getSeatNumber()); // seat_id varsa özel id al
            stmt.setString(4, reservation.getReservationDateTime().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
