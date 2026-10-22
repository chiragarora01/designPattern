package org.practice.decorator.decorator1;

public class Lattee implements Coffee{
    @Override
    public String description() {
        return "Latte";
    }

    @Override
    public double cost() {
        return 20d;
    }
}

