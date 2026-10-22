package org.practice.decorator.decorator1;

public class Espresso implements Coffee{
    @Override
    public String description() {
        return "Espresso";
    }

    @Override
    public double cost() {
        return 10d;
    }
}

