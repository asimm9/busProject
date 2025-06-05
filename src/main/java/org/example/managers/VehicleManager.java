package org.example.managers;

import org.example.helper.dao.VehicleDAO;
import org.example.models.Vehicle;
import org.example.models.VehicleType;

import java.util.List;

public class VehicleManager {

    private static VehicleManager instance;
    private VehicleDAO vehicleDAO = new VehicleDAO();

    //singleton bir nesne yaratmak için kontrol ve yaratma noktası
    public static VehicleManager getInstance(){
        if (instance == null) {
            instance = new VehicleManager();
        }
        return instance;
    }

    // sadece admin otobüsleri veya uçakları ekleyebilir.
    public boolean insertVehicle(Vehicle vehicle){
        return vehicleDAO.insertVehicle(vehicle);
    }

    //yine admin panelinde tüm uçak ve otobüsleri listeler.
    public List<Vehicle> getAllVehicles(){
        return vehicleDAO.getAllVehicles();
    }

    //otobüs veya uçağı siler.
    public boolean deleteVehicle(Vehicle vehicle){
        return vehicleDAO.deleteVehicle(vehicle);
    }

    //idye göre ve uçak veya otobüs olmasına göre aracı getirir.
    public Vehicle getVehicleById(String id, VehicleType vehicleType){
        return vehicleDAO.getVehicle(id,vehicleType);
    }

}