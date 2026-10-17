package org.practice.strategy.strategy3.strategy;

import org.practice.strategy.strategy3.VehicleType;

import java.util.Objects;

public class User{
        String id; String name; VehicleType vehicleType; RideFairStrategy rideFairStrategy;

    public User(String id, String name, VehicleType vehicleType, RideFairStrategy rideFairStrategy) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.vehicleType = Objects.requireNonNull(vehicleType);
        this.rideFairStrategy = Objects.requireNonNull(rideFairStrategy);
        if (!vehicleType.equals(rideFairStrategy.vehicleType()))
            throw new IllegalArgumentException("Vehicle type mismatch" + vehicleType + " " + rideFairStrategy.vehicleType()+" "+ id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public RideFairStrategy getRideFairStrategy() {
        return rideFairStrategy;
    }

    public void setRideFairStrategy(RideFairStrategy rideFairStrategy) {
        this.rideFairStrategy = rideFairStrategy;
    }
}
