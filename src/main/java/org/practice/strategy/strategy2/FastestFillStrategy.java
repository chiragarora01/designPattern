package org.practice.strategy.strategy2;

import java.util.Optional;

public class FastestFillStrategy implements RoutingStrategy {

    private final TradingVenueDao tradingVenueDao;

    public FastestFillStrategy(TradingVenueDao tradingVenueDao) {
        this.tradingVenueDao = tradingVenueDao;
    }

    @Override
    public Optional<TradingVenue> route(OrderDetails details) {
        return tradingVenueDao.getLowestLatencyVenue();
    }
}
