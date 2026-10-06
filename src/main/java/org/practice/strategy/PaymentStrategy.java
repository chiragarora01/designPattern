package org.practice.strategy;

public interface PaymentStrategy {

    Invoice pay(double amount);
}
