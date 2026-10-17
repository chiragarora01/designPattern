package org.practice.strategy.strategy3.strategy;

import org.practice.strategy.strategy3.Invoice;
import org.practice.strategy.strategy3.VehicleType;

import java.time.Duration;
import java.util.Optional;

public class SurgeFairStrategy implements RideFairStrategy {

    RideFairStrategy rideFairStrategy;
    double surgeFees;

    public SurgeFairStrategy(RideFairStrategy rideFairStrategy, double surgeFees) {
        this.rideFairStrategy = rideFairStrategy;
        this.surgeFees = surgeFees;
    }

    @Override
    public double getBaseFare() {
        return rideFairStrategy.getBaseFare();
    }

    @Override
    public double getPerKmFare() {
        return rideFairStrategy.getPerKmFare();
    }

    @Override
    public double getPerMinFare() {
        return rideFairStrategy.getPerMinFare();
    }

    @Override
    public VehicleType vehicleType() {
        return rideFairStrategy.vehicleType();
    }

    @Override
    public Invoice pay(double totalKmDriver, Duration time) {
        Invoice pay = rideFairStrategy.pay(totalKmDriver, time);
        double finalTotalAmount = pay.totalAmount() * surgeFees;
        return new Invoice(pay.totalDistanceInKm(), pay.time(), pay.vehicleType(), pay.baseFare(), pay.perKmCharges(), pay.timeCharges(), Optional.of(surgeFees), finalTotalAmount);
    }
}
