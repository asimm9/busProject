package org.example.models;

public enum VehicleType {
    Plane("Plane"),
    Bus("Bus");

    private final String name;
    VehicleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
