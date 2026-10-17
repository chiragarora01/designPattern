package org.practice.factory.factory3;

public class Bike implements Vehicle {


    private final VehicleDetails vehicleDetails;

    public Bike(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    @Override
    public ParkingSize getParkingSize() {
        return ParkingSize.SMALL;
    }
}
