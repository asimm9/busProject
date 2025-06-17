package org.example.models.factory;

import org.example.models.Bus;
import org.example.models.Plane;
import org.example.models.Vehicle;
import org.example.models.VehicleType;

public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }

        return switch (type) {
            case VehicleType.Bus -> new Bus();
            case VehicleType.Plane -> new Plane();
            default -> throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        };
    }
}
