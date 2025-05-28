package org.example.models;

import java.util.Objects;

public class Seat {
    private String seatID;
    private boolean isReserved;
    private int row;
    private int column;
    private String busID;
    private String tripID;
    private String userID;

    @Override
    public String toString() {
        return "Seat{" +
                "seatID='" + seatID + '\'' +
                ", isReserved=" + isReserved +
                ", row=" + row +
                ", column=" + column +
                ", busID='" + busID + '\'' +
                ", tripID='" + tripID + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seat seat)) return false;
        return isReserved() == seat.isReserved() && getRow() == seat.getRow() && getColumn() == seat.getColumn() && Objects.equals(getSeatID(), seat.getSeatID()) && Objects.equals(getBusID(), seat.getBusID()) && Objects.equals(getTripID(), seat.getTripID()) && Objects.equals(getUserID(), seat.getUserID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeatID(), isReserved(), getRow(), getColumn(), getBusID(), getTripID(), getUserID());
    }

    public String getUserID() {return userID;}

    public void setUserID(String userID) {this.userID = userID;}

    public String getBusID() {return busID;}

    public void setBusID(String busID) {this.busID = busID;}

    public String getTripID() {return tripID;}

    public void setTripID(String tripID) {this.tripID = tripID;}

    public String getSeatID() {return seatID;}

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
