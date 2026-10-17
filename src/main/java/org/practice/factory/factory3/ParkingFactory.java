package org.practice.factory.factory3;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class ParkingFactory {
    private final Map<VehicleType, Function<VehicleDetails, Vehicle>> map = new EnumMap<>(VehicleType.class);

    public ParkingFactory() {
        map.put(VehicleType.CAR, Car::new);
        map.put(VehicleType.BIKE, Bike::new);
        map.put(VehicleType.TRUCK, Truck::new);
    }

    // extension point: new types register here, factory code stays untouched
    public void registerVehicle(VehicleType type, Function<VehicleDetails, Vehicle> constructor) {
        map.put(type, constructor);
    }

    public Vehicle getVehicle(VehicleDetails details) {
        Function<VehicleDetails, Vehicle> constructor = map.get(details.vehicleType());
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown vehicle type: " + details.vehicleType());
        }
        // the EV rule lives here once — not an if-else per vehicle/feature combination
        return details.isEV() ? new EV(details) : constructor.apply(details);
    }
}
