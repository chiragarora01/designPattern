package org.practice.builder.builder2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    public enum Crust { THIN, THICK, CHEESE_BURST }
    public enum Sauce { TOMATO, PESTO, BBQ }
    public enum Cheese { MOZZARELLA, CHEDDAR, VEGAN }

    private final Size size;
    private final Crust crust;
    private final Sauce sauce;
    private final Cheese cheese;
    private final List<String> toppings;

    private Pizza(Builder b) {
        this.size = b.size;
        this.crust = b.crust;
        this.sauce = b.sauce;
        this.cheese = b.cheese;
        this.toppings = List.copyOf(b.toppings); // immutable
    }

    public double price() {
        double p = switch (size) { case SMALL -> 8; case MEDIUM -> 10; case LARGE -> 12; };
        p += crust == Crust.CHEESE_BURST ? 2 : 0;
        p += toppings.size() * 1.5;
        return p;
    }

    public static Builder builder() { return new Builder(); }

    // PRESETS: return Builder, not Pizza -> caller can still change fields
    public static Builder margherita() {
        return builder().size(Size.MEDIUM).crust(Crust.THIN)
                .sauce(Sauce.TOMATO).cheese(Cheese.MOZZARELLA)
                .addTopping("basil");
    }

    public static Builder farmhouse() {
        return builder().size(Size.MEDIUM).crust(Crust.THICK)
                .sauce(Sauce.TOMATO).cheese(Cheese.MOZZARELLA)
                .addTopping("onion").addTopping("capsicum")
                .addTopping("mushroom").addTopping("tomato");
    }

    public static class Builder {
        private Size size;
        private Crust crust;
        private Sauce sauce;
        private Cheese cheese;
        private final List<String> toppings = new ArrayList<>();

        public Builder size(Size s)       { this.size = s; return this; }
        public Builder crust(Crust c)     { this.crust = c; return this; }
        public Builder sauce(Sauce s)     { this.sauce = s; return this; }
        public Builder cheese(Cheese c)   { this.cheese = c; return this; }
        public Builder addTopping(String t) { this.toppings.add(t); return this; }
        public Builder removeTopping(String t) { this.toppings.remove(t); return this; }

        public Pizza build() {
            // validate required fields here
            Objects.requireNonNull(size, "size required");
            Objects.requireNonNull(crust, "crust required");
            Objects.requireNonNull(sauce, "sauce required");
            Objects.requireNonNull(cheese, "cheese required");
            return new Pizza(this);
        }
    }
}
