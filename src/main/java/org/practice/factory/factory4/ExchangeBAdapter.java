package org.practice.factory.factory4;

/** Exchange B sends prices as formatted strings ("1,234.50") — normalizes here. */
public class ExchangeBAdapter implements ExchangeAdapter {

    private final String price;
    private final String amount;
    private final SecurityType securityType;

    public ExchangeBAdapter(String price, String amount, SecurityType securityType) {
        this.price = price;
        this.amount = amount;
        this.securityType = securityType;
    }

    @Override
    public Message translate() {
        return new Message.Builder()
                .price(Double.parseDouble(price.replace(",", "")))
                .amount(Double.parseDouble(amount))
                .securityType(securityType)
                .build();
    }
}
