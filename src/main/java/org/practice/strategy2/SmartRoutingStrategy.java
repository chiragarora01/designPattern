package org.practice.strategy2;

import java.util.List;
import java.util.Optional;

/**
 * Twist: tries each rule in order and takes the first that can route.
 * Built from the existing rules, no selection logic of its own.
 */
public class SmartRoutingStrategy implements RoutingStrategy {

    private final List<RoutingStrategy> rules;

    public SmartRoutingStrategy(List<RoutingStrategy> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public Optional<TradingVenue> route(OrderDetails details) {
        for (RoutingStrategy rule : rules) {
            Optional<TradingVenue> venue = rule.route(details);
            if (venue.isPresent()) {
                return venue;
            }
        }
        return Optional.empty();
    }
}
