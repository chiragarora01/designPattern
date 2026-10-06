package org.practice.strategy2;

public record TradingVenue(String id, String name, double spread, double fees, double latency,
                           boolean up) {
}
