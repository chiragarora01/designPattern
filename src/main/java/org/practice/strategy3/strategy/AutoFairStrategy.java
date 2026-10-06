package org.practice.strategy3.strategy;

import org.practice.strategy3.Invoice;
import org.practice.strategy3.VehicleType;

import java.time.Duration;
import java.util.Optional;

public class AutoFairStrategy implements RideFairStrategy {
    @Override
    public double getBaseFare() {
        return 20;
    }

    @Override
    public double getPerKmFare() {
        return 30;
    }

    @Override
    public double getPerMinFare() {
        return 40;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.Auto;
    }

    @Override
    public Invoice pay(double totalKmDriver, Duration time) {
        double amount = getBaseFare();
        amount += (totalKmDriver * getPerKmFare());
        amount += (time.toMinutes() * getPerMinFare());
        return new Invoice(totalKmDriver, time, vehicleType(), getBaseFare(), getPerKmFare(), getPerMinFare(), Optional.empty(), amount);
    }
}