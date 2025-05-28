package org.example.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private String id;
    private UserModel user;
    private Trip trip;
    private Seat seat;
    private LocalDateTime reservationDateTime;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user=" + user +
                ", trip=" + trip +
                ", seat=" + seat +
                ", reservationDateTime=" + reservationDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;
        return getId() == that.getId() && Objects.equals(getUser(), that.getUser()) && Objects.equals(getTrip(), that.getTrip()) && Objects.equals(getSeat(), that.getSeat()) && Objects.equals(getReservationDateTime(), that.getReservationDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getTrip(), getSeat(), getReservationDateTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {this.reservationDateTime = reservationDateTime;}

}
