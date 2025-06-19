package org.example.managers;

import org.example.helper.dao.ReservationDAO;
import org.example.helper.dao.TripDAO;
import org.example.models.Trip;
import org.example.models.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class TripManager {
    private static TripManager instance;
    private TripDAO tripDAO = new TripDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();

    //singelton nesne burda üretilir.
    public static TripManager getInstance() {
        if (instance == null) {
            instance = new TripManager();
        }
        return instance;
    }


    //database'e Trip eklemek için kullanılır return type
    public boolean createTrip(Trip trip) {return tripDAO.insertTrip(trip);}

    // verilen trip nesnesini databaseden kaldırır eğer başarılıysa true döner.
    public boolean deleteTrip(Trip trip){
        return tripDAO.deleteTrip(trip) && reservationDAO.deleteReservationByTripId(trip.getTripID());
    }

    //Databasedeki tüm trip nesnelerini liste olarak döner.
    public List<Trip> getAllTrips(VehicleType vehicleType){return tripDAO.getAllTrips(vehicleType);}

    //ID'ye göre databaseden tek bir trip nesnesini çeker.
    public Trip getTripById(String id,VehicleType vehicleType){return tripDAO.getTrip(id,vehicleType);}

    //nerden nereye filtrelerken bu metod kullanılır ve ona ggöre seferler listelenir.
    public List<Trip> getTripByFilteredParameters(String from, String to,String departureTime, VehicleType vehicleType){
        return tripDAO.getTripByFilteredParameters(from, to, departureTime, vehicleType);}


    public Trip getTripByBusId(String vehicleid, VehicleType vehicleType){return tripDAO.getTripByBusId(vehicleid, vehicleType);}


}
