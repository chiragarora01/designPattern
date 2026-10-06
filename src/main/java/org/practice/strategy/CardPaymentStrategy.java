package org.practice.strategy;

public class CardPaymentStrategy implements PaymentStrategy{
    @Override
    public Invoice pay(double amount) {
        return new Invoice(amount,10, amount+10);
    }
}
