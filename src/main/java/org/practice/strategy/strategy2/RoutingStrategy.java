package org.practice.strategy.strategy2;

import java.util.Optional;

public interface RoutingStrategy {

    /** Chosen venue, or empty when this rule cannot route the order. */
    Optional<TradingVenue> route(OrderDetails details);
}
