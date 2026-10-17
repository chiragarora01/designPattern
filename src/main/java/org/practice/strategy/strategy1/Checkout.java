package org.practice.strategy.strategy1;

public class Checkout {

    private final PaymentStrategy strategy;

    public Checkout(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public Invoice checkout(double amount) {
        return strategy.pay(amount);
    }
}
