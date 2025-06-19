package org.example.models.interfaces;

public class EconomyClass implements SeatClassStrategy{
    @Override
    public String getClassName() {
        return "Economy";
    }

    @Override
    public double getPrice(int defaultPrice) {
        return  defaultPrice * 1;
    }

}
