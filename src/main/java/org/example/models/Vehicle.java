package org.example.models;


import java.util.Objects;

abstract public class Vehicle {
    private String id;
    private Seat[][] seatLayout;
    private int totalSeats;
    private String seatType;
    private VehicleType vehicleType;

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

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", seatLayout=" + seatLayout +
                ", totalSeats=" + totalSeats +
                ", seatType='" + seatType + '\'' +
                ", vehicleType=" + vehicleType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return getTotalSeats() == vehicle.getTotalSeats() && Objects.equals(getId(), vehicle.getId()) && Objects.equals(getSeatLayout(), vehicle.getSeatLayout()) && Objects.equals(getSeatType(), vehicle.getSeatType()) && getVehicleType() == vehicle.getVehicleType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSeatLayout(), getTotalSeats(), getSeatType(), getVehicleType());
    }
}
