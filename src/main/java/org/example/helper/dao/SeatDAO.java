package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Seat;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public static Seat getSeatById(int seatId) {
        String sql = "SELECT * FROM seats WHERE seat_id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatID(rs.getString("seat_id"));
                seat.setRow(rs.getInt("row_number"));
                seat.setColumn(rs.getInt("column_number"));
                seat.setReserved(rs.getBoolean("is_reserved"));
                seat.setUserID(rs.getString("user_id"));
                seat.setBusID(rs.getString("bus_id"));
                seat.setTripID(rs.getString("trip_id"));
                return seat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Seat> getAllSeatsByTrip(String busID) {
        String sql = "SELECT * FROM seats WHERE bus_id = ?";
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql) ) {
            stmt.setString(1, busID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatID(rs.getString("seat_id"));
                seat.setRow(rs.getInt("row_number"));
                seat.setColumn(rs.getInt("column_number"));
                seat.setReserved(rs.getBoolean("is_reserved"));
                seat.setUserID(rs.getString("user_id"));
                seat.setBusID(rs.getString("bus_id"));
                seat.setTripID(rs.getString("trip_id"));
                seats.add(seat);
            }
            return seats;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertsSeatByTrip(List<Seat> seats) {
        String sql = "INSERT INTO seats(seat_id, row_number, column_number, is_reserved, user_id, trip_id, bus_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){

            for (Seat seat : seats) {
                stmt.setString(1, seat.getSeatID());
                stmt.setInt(2, seat.getRow());
                stmt.setInt(3, seat.getColumn());
                stmt.setInt(4, seat.isReserved() ? 1 : 0);
                stmt.setString(5, seat.getUserID());
                stmt.setString(6,seat.getTripID());
                stmt.setString(7,seat.getBusID());
                stmt.addBatch();
            }
            int[] result = stmt.executeBatch();
            for (int i : result) {
                if (i != 1) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
