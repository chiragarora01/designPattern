package org.practice.decorator.decorator1;

public class Cream extends CoffeeDecorator {


    public Cream(Coffee c) {
        super(c);
        Coffee base = c;
        while (base instanceof CoffeeDecorator d) {
            base = d.wrapper;
        }
        if (base instanceof Espresso) {
            throw new IllegalArgumentException("No whipped cream on espresso");
        }
    }

    @Override
    public String description() {
        return wrapper.description() + " + Cream";
    }

    @Override
    public double cost() {
        return 15d + wrapper.cost();
    }
}
