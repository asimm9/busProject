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

    //idye göre tek bir seat getirir dbden
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
                seat.setVehicleID(rs.getString("vehicle_id"));
                seat.setTripID(rs.getString("trip_id"));
                return seat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //dbden bir otobüse ait olan tüm seatleri getirir ve ArrayList olarak döndürür
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
                seat.setVehicleID(rs.getString("vehicle_id"));
                seat.setTripID(rs.getString("trip_id"));
                seats.add(seat);
            }
            return seats;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //kullanıcı koltuk seçtikten sonra veya koltuğunu iptal ettikten sonra dbde o seati günceller
    public boolean updatesSeatByTrip(List<Seat> seats) {

        String sql = "UPDATE seats SET is_reserved = ?, user_id = ?, trip_id = ? WHERE seat_id = ? AND bus_id = ?";



        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){

            for (Seat seat : seats) {
                stmt.setInt(1, seat.isReserved() ? 1 : 0);
                stmt.setString(2, seat.getUserID());
                stmt.setString(3, seat.getTripID());
                stmt.setString(4, seat.getSeatID());
                stmt.setString(5, seat.getVehicleID());
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

    //admin her otobüse göre seatleri otobüs tipi ve koltuk sayısına göre dbye ekler
    public boolean insertSeatByBusID(Seat[][] seats) {
    String sql = "INSERT INTO seats(seat_id, row_number, column_number, is_reserved, user_id, trip_id, bus_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){
        for (int row = 0; row < seats.length; row++) {
            for (int column = 0; column < seats[row].length; column++) {
                stmt.setString(1, seats[row][column].getSeatID());
                stmt.setInt(2,  seats[row][column].getRow());
                stmt.setInt(3,  seats[row][column].getColumn());
                stmt.setInt(4,  seats[row][column].isReserved() ? 1 : 0);
                stmt.setString(5,  seats[row][column].getUserID());
                stmt.setString(6, seats[row][column].getTripID());
                stmt.setString(7, seats[row][column].getVehicleID());
                stmt.addBatch();
            }

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

    public List<Seat> getSeatByTripAndUserID(String tripID, String userID) {
        String sql = "SELECT * FROM seats WHERE trip_id = ? AND user_id = ?";
        List<Seat> seatList = new ArrayList();
        try (Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, tripID);
            stmt.setString(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatID(rs.getString("seat_id"));
                seat.setRow(rs.getInt("row_number"));
                seat.setColumn(rs.getInt("column_number"));
                seat.setReserved(rs.getBoolean("is_reserved"));
                seat.setUserID(rs.getString("user_id"));
                seat.setVehicleID(rs.getString("vehicle_id"));
                seat.setTripID(rs.getString("trip_id"));
                seat.setSeatID(rs.getString("seat_id"));
                seatList.add(seat);
            }
            return seatList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
