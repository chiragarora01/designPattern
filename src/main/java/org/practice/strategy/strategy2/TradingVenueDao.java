package org.practice.strategy.strategy2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TradingVenueDao {

    private final List<TradingVenue> database = new ArrayList<>();

    // Assume complex logic to find the best venue
    public TradingVenueDao() {
        database.add(new TradingVenue("1", "JP", 1, 2, 3, true));
        database.add(new TradingVenue("2", "JPMC", 2, 3, 4, true));
        database.add(new TradingVenue("3", "MS", 3, 5, 7, true));
    }

    public void setVenueUp(String id, boolean up) {
        for (int i = 0; i < database.size(); i++) {
            TradingVenue v = database.get(i);
            if (v.id().equals(id)) {
                database.set(i, new TradingVenue(v.id(), v.name(), v.spread(), v.fees(),
                        v.latency(), up));
                return;
            }
        }
    }

    /** The best-priced venue, or empty when that venue is down. */
    public Optional<TradingVenue> getBestPriceVenue() {
        return database.stream()
                .min(Comparator.comparingDouble(TradingVenue::spread))
                .filter(TradingVenue::up);
    }

    public Optional<TradingVenue> getLowestFeeVenue() {
        return pickLowest(Comparator.comparingDouble(TradingVenue::fees));
    }

    public Optional<TradingVenue> getLowestLatencyVenue() {
        return pickLowest(Comparator.comparingDouble(TradingVenue::latency));
    }

    private Optional<TradingVenue> pickLowest(Comparator<TradingVenue> by) {
        return database.stream().filter(TradingVenue::up).min(by);
    }
}
