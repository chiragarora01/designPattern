package org.practice.strategy2;

import java.util.Optional;

public class Broker {

    public Optional<TradingVenue> routeOrder(User client, OrderDetails details) {
        return client.routingStrategy().route(details);
    }
}
