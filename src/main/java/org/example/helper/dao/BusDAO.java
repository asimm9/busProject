package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    //dbye yeni bir otobüs eklemek için kullanılır.
    public boolean insertBus(Bus bus) {
        String sql = "INSERT INTO buses(bus_id, bus_type, total_seats) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bus.getBusID());
            stmt.setString(2, bus.getBusType());
            stmt.setInt(3, bus.getTotalSeats());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bus> getAllBuses(){
        String sql = "SELECT * FROM buses";

        List<Bus> buses = new ArrayList<>();

        try(Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql) ) {

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Bus bus = new Bus();
                bus.setBusID(rs.getString("bus_id"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setTotalSeats(rs.getInt("total_seats"));
                buses.add(bus);

            }
            return buses;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean deleteBus(Bus bus) {
        String sql = "DELETE FROM buses WHERE bus_id = ?";
        try(Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bus.getBusID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
