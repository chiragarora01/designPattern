package org.practice.strategy3;

import java.time.Duration;
import java.util.Optional;

public record Invoice(double totalDistanceInKm, Duration time,VehicleType vehicleType, double baseFare, double perKmCharges, double timeCharges,
                      Optional<Double> surgeAmount,double totalAmount ) {


}
