package org.practice.observer.observer2;

import org.practice.observer.observer2.service.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher {

    Map<Service, Subscription> listenerMap = new ConcurrentHashMap<>();

    public void subscribe(Service service, PriceListener priceListener, Subscription.Mode mode) {
        listenerMap.putIfAbsent(service, new Subscription(service, priceListener, mode));
    }

    public void publish(PriceEvent priceEvent) {
        // Filtering + delivery policy both live in the Subscription, so a slow
        // consumer can never hold up this loop.
        listenerMap.forEach((k, v) -> v.offer(priceEvent));
    }

    public void close() {
        listenerMap.values().forEach(Subscription::close);
    }

}
