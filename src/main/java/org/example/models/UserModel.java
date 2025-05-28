package org.example.models;

import java.util.List;
import java.util.Objects;

public class UserModel {

    private String username;
    private String password;
    private String email;
    private String id;
    private boolean admin;
    private List<Reservation> reservations;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserModel userModel)) return false;
        return isAdmin() == userModel.isAdmin() && Objects.equals(getUsername(), userModel.getUsername())
                && Objects.equals(getPassword(), userModel.getPassword()) && Objects.equals(getEmail(), userModel.getEmail())
                && Objects.equals(getId(), userModel.getId()) && Objects.equals(getReservations(), userModel.getReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getId(), isAdmin(), getReservations());
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", reservations=" + reservations +
                ", admin=" + admin +
                '}';
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


}
