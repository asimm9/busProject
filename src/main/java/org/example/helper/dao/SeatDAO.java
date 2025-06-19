package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Seat;
import org.example.models.interfaces.EconomyClass;
import org.example.models.interfaces.SeatClassStrategy;
import org.example.models.interfaces.VipClass;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public static SeatClassStrategy getSeatClassFromDbValue(String seatType) {
        if ("VIP".equalsIgnoreCase(seatType)) {
            return new VipClass();
        } else {
            return new EconomyClass();
        }
    }

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
                seat.setSeatClass(getSeatClassFromDbValue(rs.getString("seat_type")));
                return seat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //dbden bir otobüse ait olan tüm seatleri getirir ve ArrayList olarak döndürür
    public List<Seat> getAllSeatsByTrip(String busID) {
        String sql = "SELECT * FROM seats WHERE vehicle_id = ?";
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
                seat.setSeatClass(getSeatClassFromDbValue(rs.getString("seat_type")));
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
        String sql = "UPDATE seats SET is_reserved = ?, user_id = ?, trip_id = ?, seat_type = ? WHERE seat_id = ? AND vehicle_id = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){

            for (Seat seat : seats) {
                stmt.setInt(1, seat.isReserved() ? 1 : 0);
                stmt.setString(2, seat.getUserID());
                stmt.setString(3, seat.getTripID());
                stmt.setString(4, seat.getSeatClass().getClassName());
                stmt.setString(5, seat.getSeatID());
                stmt.setString(6, seat.getVehicleID());
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
        String sql = "INSERT INTO seats(seat_id, row_number, column_number, is_reserved, user_id, trip_id, vehicle_id, seat_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){
            for (int row = 0; row < seats.length; row++) {
                for (int column = 0; column < seats[row].length; column++) {
                    Seat s = seats[row][column];
                    stmt.setString(1, s.getSeatID());
                    stmt.setInt(2,  s.getRow());
                    stmt.setInt(3,  s.getColumn());
                    stmt.setInt(4,  s.isReserved() ? 1 : 0);
                    stmt.setString(5,  s.getUserID());
                    stmt.setString(6, s.getTripID());
                    stmt.setString(7, s.getVehicleID());
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

    //sefer ve kullanıcıya göre seçilen koltuğu getirir.
    public List<Seat> getSeatByTripAndUserID(String tripID) {
        String sql = "SELECT * FROM seats WHERE trip_id = ?";
        List<Seat> seatList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, tripID);
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
                seat.setSeatClass(getSeatClassFromDbValue(rs.getString("seat_type")));
                seatList.add(seat);
            }
            return seatList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Seat> getSeatByTripAndUserID(String tripID, String userID) {
        String sql = "SELECT * FROM seats WHERE trip_id = ? AND user_id = ?";
        List<Seat> seatList = new ArrayList<>();
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
                seat.setSeatClass(getSeatClassFromDbValue(rs.getString("seat_type")));
                seatList.add(seat);
            }
            return seatList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean deleteSeatsByBusId(String vehicleID){
        String sql = "DELETE FROM seats WHERE vehicle_id = ?";
        boolean result = false;
        try (Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, vehicleID);
            int affectedRows = stmt.executeUpdate();
            System.out.println(affectedRows + " rows affected");
            result = affectedRows > 0;
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

}
