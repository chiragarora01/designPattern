package org.practice.strategy.strategy2;

import java.util.Optional;

public class LowestFeesStrategy implements RoutingStrategy {

    private final TradingVenueDao tradingVenueDao;

    public LowestFeesStrategy(TradingVenueDao tradingVenueDao) {
        this.tradingVenueDao = tradingVenueDao;
    }

    @Override
    public Optional<TradingVenue> route(OrderDetails details) {
        return tradingVenueDao.getLowestFeeVenue();
    }
}
