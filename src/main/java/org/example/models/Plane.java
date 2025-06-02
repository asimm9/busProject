package org.example.models;

public class Plane extends Vehicle {

    @Override
    public  String toString() {
        return "Plane{" +
                "planeId= " + getId()+
                ", total Seats = " + getTotalSeats()+
                "plane type: " + getSeatType() + "\'" +
                "}";
    }

}
