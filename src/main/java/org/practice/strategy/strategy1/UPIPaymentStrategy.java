package org.practice.strategy.strategy1;

public class UPIPaymentStrategy implements PaymentStrategy {
    @Override
    public Invoice pay(double amount) {
        return new Invoice(amount, 0, amount + 0);
    }
}
