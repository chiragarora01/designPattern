package org.practice.strategy.strategy2;

public record TradingVenue(String id, String name, double spread, double fees, double latency,
                           boolean up) {
}
