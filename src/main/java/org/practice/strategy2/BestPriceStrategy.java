package org.practice.strategy2;

import java.util.Optional;

public class BestPriceStrategy implements RoutingStrategy {

    private final TradingVenueDao tradingVenueDao;

    public BestPriceStrategy(TradingVenueDao tradingVenueDao) {
        this.tradingVenueDao = tradingVenueDao;
    }

    @Override
    public Optional<TradingVenue> route(OrderDetails details) {
        return tradingVenueDao.getBestPriceVenue();
    }
}
