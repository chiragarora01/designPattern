package org.practice.factory.factory3;

public class EV extends Car{

    public EV(VehicleDetails vehicleDetails) {
        super(vehicleDetails);
    }

    @Override
    public boolean needsCharging() {
        return true;
    }
}
