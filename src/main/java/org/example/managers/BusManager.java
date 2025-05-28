package org.example.managers;

import org.example.helper.dao.BusDAO;
import org.example.models.Bus;

public class BusManager {

    private static BusManager instance;
    private BusDAO busDAO = new BusDAO();

    public static BusManager getInstance(){
        if (instance == null) {
            instance = new BusManager();
        }
        return instance;
    }

    // sadece admin otobüsleri ekleyebilir.
    public boolean insertBus(Bus bus){
        return busDAO.insertBus(bus);
    }





}
