package org.practice.strategy.strategy1;

public class WalletStrategy implements PaymentStrategy{

    @Override
    public Invoice pay(double amount) {
        return new Invoice(amount,5,amount+5);
    }
}
