package org.example.models;

import java.util.Objects;

public class Seat {
    private int seatNumber;
    private boolean isReserved;
    private String passengerName;
    private int row;
    private int column;

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber=" + seatNumber +
                ", isReserved=" + isReserved +
                ", passengerName='" + passengerName + '\'' +
                ", row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seat seat)) return false;
        return getSeatNumber() == seat.getSeatNumber() && isReserved() == seat.isReserved() && getRow() == seat.getRow() && getColumn() == seat.getColumn() && Objects.equals(getPassengerName(), seat.getPassengerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeatNumber(), isReserved(), getPassengerName(), getRow(), getColumn());
    }
}
