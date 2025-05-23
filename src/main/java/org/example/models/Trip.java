package org.example.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Trip {

    private int tripID;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Bus bus;
    private List<Seat> seatList;

    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripID=" + tripID +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", bus=" + bus +
                ", seatList=" + seatList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trip trip)) return false;
        return getTripID() == trip.getTripID() && Objects.equals(getOrigin(), trip.getOrigin()) && Objects.equals(getDestination(), trip.getDestination()) && Objects.equals(getDepartureTime(), trip.getDepartureTime()) && Objects.equals(getBus(), trip.getBus()) && Objects.equals(getSeatList(), trip.getSeatList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTripID(), getOrigin(), getDestination(), getDepartureTime(), getBus(), getSeatList());
    }
}
