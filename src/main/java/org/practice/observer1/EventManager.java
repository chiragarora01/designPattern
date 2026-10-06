package org.practice.observer1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private final Map<User, Alert> alerts = new ConcurrentHashMap<>();

    public void addAlert(User u, Alert a) {
        alerts.putIfAbsent(u, a);
    }

    public void removeAlert(User u) {
        alerts.remove(u);
    }

    public void onTick(String symbol, double price) {
        alerts.forEach((k, v) -> {
            if (v.symbol().equals(symbol)) v.evaluate(price);
        });
    }
}
