package org.practice.decorator.decorator1;

public class CoffeeDecorator implements Coffee {
    protected final Coffee wrapper;

    public CoffeeDecorator(Coffee wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public String description() {
        return wrapper.description();
    }

    @Override
    public double cost() {
        return wrapper.cost();
    }
}

