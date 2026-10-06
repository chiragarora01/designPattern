package org.practice.strategy;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentStrategy implements PaymentStrategy {

    public record Part(PaymentStrategy strategy, double amount) {
    }

    private final List<Part> parts;

    public SplitPaymentStrategy(List<Part> parts) {
        this.parts = List.copyOf(parts);
    }

    @Override
    public Invoice pay(double amount) {
        double allocated = parts.stream().mapToDouble(Part::amount).sum();
        if (allocated != amount) {
            throw new IllegalArgumentException(
                    "split parts total " + allocated + " but order amount is " + amount);
        }

        List<Invoice> list = new ArrayList<>();
        for (Part p : parts) {
            list.add(p.strategy().pay(p.amount()));
        }

        double amountTotal = 0;
        double markup = 0;
        double totalAmount = 0;
        for (Invoice v : list) {
            amountTotal += v.amount();
            markup += v.markup();
            totalAmount += v.finalAmount();
        }
        return new Invoice(amountTotal, markup, totalAmount);
    }
}
