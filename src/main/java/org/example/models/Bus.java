package org.example.models;

import java.util.Arrays;
import java.util.Objects;

public class Bus {
    private String busID;
    private Seat[][] seatLayout;
    private int totalSeats;
    private String busType;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bus bus)) return false;
        return getBusID() == bus.getBusID() && getTotalSeats() == bus.getTotalSeats() && Objects.deepEquals(getSeatLayout(), bus.getSeatLayout()) && Objects.equals(getBusType(), bus.getBusType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBusID(), Arrays.deepHashCode(getSeatLayout()), getTotalSeats(), getBusType());
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busID=" + busID +
                ", seatLayout=" + Arrays.toString(seatLayout) +
                ", totalSeats=" + totalSeats +
                ", busType='" + busType + '\'' +
                '}';
    }

    public String getBusID() {return busID;}

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public Seat[][] getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(Seat[][] seatLayout) {
        this.seatLayout = seatLayout;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

}
