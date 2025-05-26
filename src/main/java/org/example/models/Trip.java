package org.example.models;

import javafx.scene.control.DatePicker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Trip {

    private String tripID;
    private String origin;
    private String destination;
    private DatePicker departureTime;
    private LocalDateTime time;
    private Bus bus;
    private List<Seat> seatList;



    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
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

    public DatePicker getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(DatePicker departureTime) {
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
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trip trip)) return false;
        return Objects.equals(getTripID(), trip.getTripID()) && Objects.equals(getOrigin(), trip.getOrigin()) && Objects.equals(getDestination(), trip.getDestination()) && Objects.equals(getDepartureTime(), trip.getDepartureTime()) && Objects.equals(getTime(), trip.getTime()) && Objects.equals(getBus(), trip.getBus()) && Objects.equals(getSeatList(), trip.getSeatList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTripID(), getOrigin(), getDestination(), getDepartureTime(), getTime(), getBus(), getSeatList());
    }



}
