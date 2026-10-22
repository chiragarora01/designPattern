package org.practice.decorator.decorator1;

public class Main {
    public static void main(String[] args) {
        // double caramel charges twice
        Coffee drink = new Cream(new Caramel(new Caramel(new Milk(new Sugar(new Lattee())))));
        System.out.println(drink.description() + " = " + drink.cost());

        // rule: no whipped cream on espresso
        try {
            new Cream(new Milk(new Espresso()));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
