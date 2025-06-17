package org.example.models;


public class Plane extends Vehicle {

    private int baggage;

    public int getBaggage() {
        return baggage;
    }

    public void setBaggage(int baggage) {
        this.baggage = baggage;
    }

    @Override
    public  String toString() {
        return "Plane{" +
                "planeId= " + getId()+
                ", total Seats = " + getTotalSeats()+
                "plane type: " + getSeatType() + "\'" +
                ", baggage=" + baggage +
                "}";
    }

}
