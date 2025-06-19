package org.example.models.interfaces;

public class VipClass implements SeatClassStrategy{
    @Override
    public String getClassName() {
        return "VIP";
    }

    @Override
    public double getPrice(int defaultPrice) {
        return defaultPrice * 2;
    }


}
