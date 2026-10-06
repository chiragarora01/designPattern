package org.practice.strategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PaymentStrategy card = new CardPaymentStrategy();
        PaymentStrategy wallet = new WalletStrategy();
        PaymentStrategy upi = new UPIPaymentStrategy();

        System.out.println(new Checkout(card).checkout(500));
        System.out.println(new Checkout(wallet).checkout(500));
        System.out.println(new Checkout(upi).checkout(500));

        // Twist: split payment is just another strategy, Checkout is unchanged.
        PaymentStrategy split = new SplitPaymentStrategy(List.of(
                new SplitPaymentStrategy.Part(wallet, 200),
                new SplitPaymentStrategy.Part(card, 300)));

        System.out.println(new Checkout(split).checkout(500));
    }
}
