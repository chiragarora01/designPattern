package org.practice.observer.observer1;

public class Alert {
    private final User user;
    private final String symbol;
    private final double threshold;
    private final Observer observer;
    private boolean armed = true;

    public Alert(User user, String symbol, double threshold, Observer observer) {
        this.user = user;
        this.symbol = symbol;
        this.threshold = threshold;
        this.observer = observer;
    }

    public String symbol() {
        return symbol;
    }

    void evaluate(double price) {
        boolean breached = price >= threshold;
        if (breached && armed) {
            armed = false;
            observer.update(user, symbol + " is above " + threshold + " (" + price + ")");
        } else if (!breached) {
            armed = true;
        }
    }
}
