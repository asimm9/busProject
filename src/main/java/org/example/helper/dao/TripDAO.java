package org.example.helper.dao;

import javafx.scene.control.DatePicker;
import org.example.helper.DatabaseConnector;
import org.example.models.Trip;
import org.example.models.Vehicle;
import org.example.models.VehicleType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    //dbye yeni bir trip eklemek için kullanılır
    public boolean insertTrip(Trip trip) {
        String sql = "INSERT INTO trips(trip_id, origin, destination, departure_time, time, vehicle_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trip.getTripID());
            stmt.setString(2, trip.getOrigin());
            stmt.setString(3, trip.getDestination());
            stmt.setString(4, trip.getDepartureTime());
            stmt.setString(5, trip.getTime().toString());
            stmt.setString(6, trip.getVehicle().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //verilen tripi dbden silmek için kullanılır
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

    //dbden tüm tripleri getirir
    public List<Trip> getAllTrips() {
        String sql = "SELECT * FROM trips";
        List<Trip> trips = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql) ) {
            ResultSet rs = stmt.executeQuery();
            VehicleDAO vehicleDAO = new VehicleDAO();
            while (rs.next()) {

                String tripID = rs.getString("trip_id");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");
                String departureTime = rs.getString("departure_time");
                String time = rs.getString("time");
                String busId = rs.getString("vehicle_id");

                Trip trip = new Trip();
                trip.setTripID(tripID);
                trip.setOrigin(origin);
                trip.setDestination(destination);
                trip.setDepartureTime(departureTime);
                Vehicle vehicle = vehicleDAO.getVehicle(busId, VehicleType.Bus);
                trip.setVehicle(vehicle);
                trip.setTime(time);
                trips.add(trip);
            }
            return trips;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //id ye göre dbden trip getirmek için kullanılır
    public Trip getTrip(String tripID) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
            Trip trip = null;
            try (Connection connection = DatabaseConnector.connect(); PreparedStatement stmt = connection.prepareStatement(sql) ) {
                stmt.setString(1, tripID);
                ResultSet rs = stmt.executeQuery();
                VehicleDAO vehicleDAO = new VehicleDAO();
                if (rs.next()) {
                    trip = new Trip();
                    trip.setTripID(tripID);
                    trip.setOrigin(rs.getString("origin"));
                    trip.setDestination(rs.getString("destination"));
                    trip.setDepartureTime(rs.getString("departure_time"));
                    trip.setTime(rs.getString("time"));
                    String vehicleId = rs.getString("vehicle_id");
                    trip.setVehicle(vehicleDAO.getVehicle(vehicleId,VehicleType.Plane));
                }
                return trip;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }

    //kullanıcı kısmında tripleri filtrelemek için kullanılır nereden nereye sorusunun cevabıdır
    public List<Trip> getTripByFilteredParameters(String origin, String destination, VehicleType vehicleType) {
        String sql = "SELECT * FROM trips WHERE origin = ? AND destination = ?";
        List<Trip> trips = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, origin);
            statement.setString(2, destination);
            ResultSet rs = statement.executeQuery();
            VehicleDAO vehicleDAO = new VehicleDAO();
            while (rs.next()) {
                String tripID = rs.getString("trip_id");
                String originValue = rs.getString("origin");
                String destinationValue = rs.getString("destination");
                String vehicleIdId = rs.getString("vehicle_id");
                String departureTime = rs.getString("departure_time");
                String timeValue = rs.getString("time");

                Trip trip = new Trip();
                trip.setTripID(tripID);
                trip.setOrigin(originValue);
                trip.setDestination(destinationValue);
                trip.setDepartureTime(departureTime);
                Vehicle vehicle = vehicleDAO.getVehicle(vehicleIdId,vehicleType);
                trip.setVehicle(vehicle);
                trip.setTime(timeValue);
                if (vehicle == null) {
                    System.out.println(" bu eklenmedi");
                }else{
                    trips.add(trip);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }

}
