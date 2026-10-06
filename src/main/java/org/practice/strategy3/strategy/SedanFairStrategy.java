package org.practice.strategy3.strategy;

import org.practice.strategy3.Invoice;
import org.practice.strategy3.VehicleType;

import java.time.Duration;
import java.util.Optional;

public class SedanFairStrategy implements RideFairStrategy {
    @Override
    public double getBaseFare() {
        return 50;
    }

    @Override
    public double getPerKmFare() {
        return 60;
    }

    @Override
    public double getPerMinFare() {
        return 70;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.SEDAN;
    }

    @Override
    public Invoice pay(double totalKmDriver, Duration time) {
        double amount = getBaseFare();
        amount += (totalKmDriver * getPerKmFare());
        amount += (time.toMinutes() * getPerMinFare());
        return new Invoice(totalKmDriver, time, vehicleType(), getBaseFare(), getPerKmFare(), getPerMinFare(), Optional.empty(), amount);
    }
}

