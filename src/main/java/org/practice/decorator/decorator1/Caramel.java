package org.practice.decorator.decorator1;

public class Caramel extends CoffeeDecorator {

    public Caramel(Coffee c) {
        super(c);
    }

    @Override
    public String description() {
        return wrapper.description() + " + Caramel";
    }

    @Override
    public double cost() {
        return 8d + wrapper.cost();
    }
}
