package org.practice.factory.factory3;

public interface Vehicle {

    ParkingSize getParkingSize();

    default boolean needsCharging() {
        return false;
    }
}
