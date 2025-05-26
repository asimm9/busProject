package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Seat;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeatDAO {
    public boolean insertSeat(Seat seat, int tripId) {
        String sql = "INSERT INTO seats(seat_number, row_number, column_number, is_reserved, passenger_name, trip_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seat.getSeatNumber());
            stmt.setInt(2, seat.getRow());
            stmt.setInt(3, seat.getColumn());
            stmt.setInt(4, seat.isReserved() ? 1 : 0);
            stmt.setString(5, seat.getPassengerName());
            stmt.setInt(6, tripId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Seat getSeatById(int seatId) {
        String sql = "SELECT * FROM seats WHERE seat_id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatNumber(rs.getInt("seat_id"));
                return seat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
