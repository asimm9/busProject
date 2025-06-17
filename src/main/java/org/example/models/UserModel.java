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

    public static class  Builder {
        private UserModel userModel = new UserModel();
        public Builder username(String username) {
            this.userModel.username = username;
            return this;
        }
        public Builder password(String password) {
            this.userModel.password = password;
            return this;
        }
        public Builder email(String email) {
            this.userModel.email = email;
            return this;
        }
        public Builder id(String id) {
            this.userModel.id = id;
            return this;
        }
        public Builder admin(boolean admin) {
            this.userModel.admin = admin;
            return this;
        }
        public Builder reservations(List<Reservation> reservations) {
            this.userModel.reservations = reservations;
            return this;
        }
        public UserModel build() {
            return this.userModel;
        }
    }

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



    public String getPassword() {
        return password;
    }



    public String getEmail() {
        return email;
    }


    public String getId() {
        return id;
    }


    public List<Reservation> getReservations() {
        return reservations;
    }



}
