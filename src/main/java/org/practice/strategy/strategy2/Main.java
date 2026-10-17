package org.practice.strategy.strategy2;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TradingVenueDao dao = new TradingVenueDao();
        Broker broker = new Broker();
        OrderDetails order = new OrderDetails("ORD-1", 100.0);

        RoutingStrategy bestPrice = new BestPriceStrategy(dao);
        RoutingStrategy lowestFees = new LowestFeesStrategy(dao);
        RoutingStrategy fastestFill = new FastestFillStrategy(dao);

        System.out.println(broker.routeOrder(new User("c1", bestPrice), order));
        System.out.println(broker.routeOrder(new User("c2", lowestFees), order));
        System.out.println(broker.routeOrder(new User("c3", fastestFill), order));

        // Twist: smart rule composes the existing two, no copy-paste.
        RoutingStrategy smart = new SmartRoutingStrategy(List.of(bestPrice, fastestFill));
        User smartClient = new User("c4", smart);

        System.out.println("smart, all venues up: " + broker.routeOrder(smartClient, order));

        dao.setVenueUp("1", false); // best-price venue goes down
        System.out.println("smart, best venue down: " + broker.routeOrder(smartClient, order));
    }
}
