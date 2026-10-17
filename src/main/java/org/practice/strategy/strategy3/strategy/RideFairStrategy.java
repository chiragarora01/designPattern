package org.practice.strategy.strategy3.strategy;

import org.practice.strategy.strategy3.Invoice;
import org.practice.strategy.strategy3.VehicleType;

import java.time.Duration;

public interface RideFairStrategy {

    double getBaseFare();

    double getPerKmFare();

    double getPerMinFare();

    VehicleType vehicleType();

    Invoice pay(double totalKmDriver, Duration time);

}
