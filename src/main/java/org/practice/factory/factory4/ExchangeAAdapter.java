package org.practice.factory.factory4;

/** Exchange A already sends doubles — maps straight through. */
public class ExchangeAAdapter implements ExchangeAdapter {

    private final double price;
    private final double amount;
    private final SecurityType securityType;

    public ExchangeAAdapter(double price, double amount, SecurityType securityType) {
        this.price = price;
        this.amount = amount;
        this.securityType = securityType;
    }

    @Override
    public Message translate() {
        return new Message.Builder()
                .price(price)
                .amount(amount)
                .securityType(securityType)
                .build();
    }
}
