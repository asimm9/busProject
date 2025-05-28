package org.example.managers;

import org.example.helper.dao.TripDAO;
import org.example.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripManager {
    private static TripManager instance;
    private List<Trip> trips;
    private int tripIDcounter;
    private TripDAO tripDAO = new TripDAO();

    public static TripManager getInstance() {
        if (instance == null) {
            instance = new TripManager();
        }
        return instance;
    }

    //database'e Trip eklemek için kullanılır return type
    public boolean createTrip(Trip trip) {return tripDAO.insertTrip(trip);}

    // verilen trip nesnesini databaseden kaldırır eğer başarılıysa true döner.
    public boolean deleteTrip(Trip trip){return tripDAO.deleteTrip(trip);}

    //Databasedeki tüm trip nesnelerini liste olarak döner.
    public List<Trip> getAllTrips(){return tripDAO.getAllTrips();}

    //ID'ye göre databaseden tek bir trip nesnesini çeker.
    public Trip getTripById(String id){return tripDAO.getTrip(id);}

    public List<Trip> getTripByFilteredParameters(String from, String to){return tripDAO.getTripByFilteredParameters(from, to);}
}
