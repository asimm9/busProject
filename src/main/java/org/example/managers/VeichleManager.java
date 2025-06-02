package org.example.managers;

import org.example.helper.dao.VeichleDAO;
import org.example.models.Veihcle;
import org.example.models.VeihcleType;

import java.util.List;

public class VeichleManager {

    private static VeichleManager instance;
    private VeichleDAO veichleDAO = new VeichleDAO();

    public static VeichleManager getInstance(){
        if (instance == null) {
            instance = new VeichleManager();
        }
        return instance;
    }

    // sadece admin otobüsleri ekleyebilir.
    public boolean insertVeichle(Veihcle veihcle){
        return veichleDAO.insertVeihcle(veihcle);
    }

    public List<Veihcle> getAllVeichles(){
        return veichleDAO.getAllVeihcles();
    }

    public boolean deleteVeihcle(Veihcle veihcle){
        return veichleDAO.deleteVeihcle(veihcle);
    }

    public Veihcle getVeihcleById(String id, VeihcleType veihcleType){
        return veichleDAO.getVehicle(id,veihcleType);
    }







}
