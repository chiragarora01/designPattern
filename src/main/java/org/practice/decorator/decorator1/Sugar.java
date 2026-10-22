package org.practice.decorator.decorator1;

public class Sugar extends CoffeeDecorator {


    public Sugar(Coffee c) {
        super(c);
    }

    @Override
    public String description() {
        return wrapper.description() + " + Sugar";
    }

    @Override
    public double cost() {
        return 5d + wrapper.cost();
    }
}

