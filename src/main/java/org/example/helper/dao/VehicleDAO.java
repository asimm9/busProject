package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.Bus;
import org.example.models.Plane;
import org.example.models.Vehicle;
import org.example.models.VehicleType;
import org.example.models.factory.VehicleFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    //dbye yeni bir otobüs veya yeni bir uçak eklemek için kullanılır.
    public boolean insertVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles(vehicle_id, seat_type, total_seats, vehicle_type, price, baggage) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getId());
            stmt.setString(2, vehicle.getSeatType());
            stmt.setInt(3, vehicle.getTotalSeats());
            stmt.setString(4,vehicle.getVehicleType().toString());
            stmt.setDouble(5, vehicle.getPrice());
            if (vehicle instanceof Plane) {
                stmt.setDouble(6, ((Plane) vehicle).getBaggage());
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hem Uçak Hem de otobüsler için dbdeki tüm otobüs ve uçakları getirir liste halinde döndürür.
    public List<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicles";

        List<Vehicle> vehicles = new ArrayList<>();

        try(Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql) ) {

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Vehicle vehicle = VehicleFactory.createVehicle(VehicleType.valueOf(rs.getString("vehicle_type")));
                vehicle.setId(rs.getString("vehicle_id"));
                vehicle.setSeatType(rs.getString("seat_type"));
                vehicle.setTotalSeats(rs.getInt("total_seats"));
                String vehicleType = rs.getString("vehicle_type");
                vehicle.setVehicleType(VehicleType.valueOf(vehicleType));
                vehicle.setPrice(rs.getInt("price"));
                if (vehicle instanceof Plane) {
                    ((Plane) vehicle).setBaggage(rs.getInt("baggage"));
                }
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Hem Uçak hem de otobüsleri idlerine göre dbden siler ve return olarak boolean değer döndürür.
    public boolean deleteVehicle(Vehicle vehicle) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try(Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Tek bir uçak veya tek bir otobüse ihtiyacımız olduğunda bu metod ile id vererek ihtiyacımız olan otobüs veya uçağı return eder
    public Vehicle getVehicle(String vehicleId, VehicleType vehicleType) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ? AND vehicle_type = ?";
        Vehicle vehicle = null;
        try(Connection connection =DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            stmt.setString(2, vehicleType.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (vehicleType.equals(VehicleType.Plane)){
                    vehicle = VehicleFactory.createVehicle(VehicleType.Plane);
                }else {
                    vehicle = VehicleFactory.createVehicle(VehicleType.Bus);
                }
                vehicle.setId(rs.getString("vehicle_id"));
                vehicle.setSeatType(rs.getString("seat_type"));
                vehicle.setTotalSeats(rs.getInt("total_seats"));
                String vehicleTypee = rs.getString("vehicle_type");
                vehicle.setPrice(rs.getInt("price"));
                if (vehicle instanceof Plane) {
                    ((Plane) vehicle).setBaggage(rs.getInt("baggage"));
                }
                vehicle.setVehicleType(VehicleType.valueOf(vehicleTypee));
            }
            return vehicle;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
