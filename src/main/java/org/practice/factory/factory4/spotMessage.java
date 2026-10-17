package org.practice.factory.factory4;

public class spotMessage implements Msg {

    private final double price;
    private final SecurityType securityType;
    private final double amount;

    public spotMessage(Message message) {
        if (message.getPrice() <= 0) {
            throw new IllegalArgumentException("Spot requires a positive price");
        }
        if (message.getAmount() <= 0) {
            throw new IllegalArgumentException("Spot requires a positive amount");
        }
        this.price = message.getPrice();
        this.securityType = message.getSecurityType();
        this.amount = message.getAmount();
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }
}
