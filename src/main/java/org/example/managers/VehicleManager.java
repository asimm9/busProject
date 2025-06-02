package org.example.managers;

import org.example.helper.dao.VehicleDAO;
import org.example.models.Vehicle;
import org.example.models.VehicleType;

import java.util.List;

public class VehicleManager {

    private static VehicleManager instance;
    private VehicleDAO vehicleDAO = new VehicleDAO();

    public static VehicleManager getInstance(){
        if (instance == null) {
            instance = new VehicleManager();
        }
        return instance;
    }

    // sadece admin otobüsleri ekleyebilir.
    public boolean insertVehicle(Vehicle vehicle){
        return vehicleDAO.insertVehicle(vehicle);
    }

    public List<Vehicle> getAllVehicles(){
        return vehicleDAO.getAllVehicles();
    }

    public boolean deleteVehicle(Vehicle vehicle){
        return vehicleDAO.deleteVehicle(vehicle);
    }

    public Vehicle getVehicleById(String id, VehicleType vehicleType){
        return vehicleDAO.getVehicle(id,vehicleType);
    }







}
