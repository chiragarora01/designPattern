package org.practice.decorator.decorator1;

public class Milk extends CoffeeDecorator {


    public Milk(Coffee c) {
        super(c);
    }

    @Override
    public String description() {
        return wrapper.description() + " + Milk";
    }

    @Override
    public double cost() {
        return 10d + wrapper.cost();
    }
}

