package org.example.models;


import org.example.models.VehicleType;

import java.util.Arrays;
import java.util.Objects;

abstract public class Vehicle {
    private String id;
    private Seat[][] seatLayout;
    private int totalSeats;
    private String seatType;
    private VehicleType vehicleType;
    private int price;


    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", seatLayout=" + Arrays.toString(seatLayout) +
                ", totalSeats=" + totalSeats +
                ", seatType='" + seatType + '\'' +
                ", vehicleType=" + vehicleType +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return getTotalSeats() == vehicle.getTotalSeats() && getPrice() == vehicle.getPrice() && Objects.equals(getId(), vehicle.getId()) && Objects.deepEquals(getSeatLayout(), vehicle.getSeatLayout()) && Objects.equals(getSeatType(), vehicle.getSeatType()) && getVehicleType() == vehicle.getVehicleType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), Arrays.deepHashCode(getSeatLayout()), getTotalSeats(), getSeatType(), getVehicleType(), getPrice());
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Seat[][] getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(Seat[][] seatLayout) {
        this.seatLayout = seatLayout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
