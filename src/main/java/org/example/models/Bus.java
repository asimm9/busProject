package org.example.models;

public class Bus extends Vehicle {

    @Override
    public String toString() {
        return "Bus{" +
                "busID=" + getId() +
                ", totalSeats=" + getTotalSeats() +
                ", busType='" + getSeatType() + '\'' +
                '}';
    }

}
