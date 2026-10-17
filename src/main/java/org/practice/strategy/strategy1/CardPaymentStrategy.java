package org.practice.strategy.strategy1;

public class CardPaymentStrategy implements PaymentStrategy{
    @Override
    public Invoice pay(double amount) {
        return new Invoice(amount,10, amount+10);
    }
}
