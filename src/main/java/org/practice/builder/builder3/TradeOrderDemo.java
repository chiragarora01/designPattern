package org.practice.builder.builder3;

import java.math.BigDecimal;
import java.util.Objects;

public class TradeOrderDemo {

    enum Side {
        BUY, SELL
    }

    enum TimeInForce {
        DAY, GTC, IOC, FOK
    }

    enum OrderType {
        MARKET, LIMIT
    }

    // Common interface for all trade orders
    interface TradeOrder {
        String symbol();
        Side side();
        int quantity();
        TimeInForce timeInForce();
        OrderType orderType();
    }

    // MARKET order: no limit-price field exists
    record MarketOrder(
            String symbol,
            Side side,
            int quantity,
            TimeInForce timeInForce
    ) implements TradeOrder {

        public MarketOrder {
            validateCommon(symbol, side, quantity, timeInForce);
        }

        @Override
        public OrderType orderType() {
            return OrderType.MARKET;
        }
    }

    // LIMIT order: limit price is mandatory
    record LimitOrder(
            String symbol,
            Side side,
            int quantity,
            BigDecimal limitPrice,
            TimeInForce timeInForce
    ) implements TradeOrder {

        public LimitOrder {
            validateCommon(symbol, side, quantity, timeInForce);

            Objects.requireNonNull(limitPrice, "Limit price is required");

            if (limitPrice.signum() <= 0) {
                throw new IllegalArgumentException(
                        "Limit price must be positive"
                );
            }
        }

        @Override
        public OrderType orderType() {
            return OrderType.LIMIT;
        }
    }

    private static void validateCommon(
            String symbol,
            Side side,
            int quantity,
            TimeInForce timeInForce) {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException(
                    "Symbol is required"
            );
        }

        Objects.requireNonNull(side, "Side is required");
        Objects.requireNonNull(
                timeInForce, "Time-in-force is required");

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be positive"
            );
        }
    }

    // Staged builder: common fields must be supplied first.
    interface SymbolStage {
        SideStage symbol(String symbol);
    }

    interface SideStage {
        QuantityStage side(Side side);
    }

    interface QuantityStage {
        TimeInForceStage quantity(int quantity);
    }

    interface TimeInForceStage {
        OrderStage timeInForce(TimeInForce tif);
    }

    interface OrderStage {
        MarketOrder market();

        LimitPriceStage limit();
    }

    interface LimitPriceStage {
        LimitOrder price(BigDecimal price);
    }

    // Builder implementation
    static class Builder implements
            SymbolStage,
            SideStage,
            QuantityStage,
            TimeInForceStage,
            OrderStage,
            LimitPriceStage {

        private String symbol;
        private Side side;
        private int quantity;
        private TimeInForce tif;

        private Builder() {}

        static SymbolStage create() {
            return new Builder();
        }

        @Override
        public SideStage symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        @Override
        public QuantityStage side(Side side) {
            this.side = side;
            return this;
        }

        @Override
        public TimeInForceStage quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        @Override
        public OrderStage timeInForce(TimeInForce tif) {
            this.tif = tif;
            return this;
        }

        @Override
        public MarketOrder market() {
            return new MarketOrder(symbol, side, quantity, tif);
        }

        @Override
        public LimitPriceStage limit() {
            return this;
        }

        @Override
        public LimitOrder price(BigDecimal price) {
            return new LimitOrder(
                    symbol, side, quantity, price, tif);
        }
    }

    public static SymbolStage tradeOrder() {
        return Builder.create();
    }

    public static void main(String[] args) {

        // Valid MARKET order
        MarketOrder marketOrder = tradeOrder()
                .symbol("AAPL")
                .side(Side.BUY)
                .quantity(100)
                .timeInForce(TimeInForce.DAY)
                .market();

        // Valid LIMIT order
        LimitOrder limitOrder = tradeOrder()
                .symbol("MSFT")
                .side(Side.SELL)
                .quantity(50)
                .timeInForce(TimeInForce.GTC)
                .limit()
                .price(new BigDecimal("425.50"));

        System.out.println(marketOrder);
        System.out.println(limitOrder);
    }
}
