package org.practice.factory.factory3;

public class Truck implements Vehicle {


    private final VehicleDetails vehicleDetails;

    public Truck(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    @Override
    public ParkingSize getParkingSize() {
        return ParkingSize.LARGE;
    }
}

