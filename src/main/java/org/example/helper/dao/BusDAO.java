package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
