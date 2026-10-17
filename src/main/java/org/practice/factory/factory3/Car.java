package org.practice.factory.factory3;

public class Car implements Vehicle {

    private final VehicleDetails vehicleDetails;

    public Car(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }


    @Override
    public ParkingSize getParkingSize() {
        return ParkingSize.MEDIUM;
    }
}

