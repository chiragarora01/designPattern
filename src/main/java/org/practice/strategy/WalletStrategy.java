package org.practice.strategy;

public class WalletStrategy implements PaymentStrategy{

    @Override
    public Invoice pay(double amount) {
        return new Invoice(amount,5,amount+5);
    }
}
