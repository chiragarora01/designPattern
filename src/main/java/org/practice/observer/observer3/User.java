package org.practice.observer.observer3;

import org.practice.observer.observer3.strategy.NotificationStrategy;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    String name;
    String id;
    // Mute is per channel, so the subscription state is per channel too.
    private final Map<String, Alert> alerts = new ConcurrentHashMap<>();

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void subscribe(EventNotifier channel, NotificationStrategy strategy) {
        // Own Alert instance per user: muting never leaks to another subscriber.
        alerts.put(channel.channelId(), new Alert(channel.channelId(), strategy));
        channel.addUserInNotificationList(this);
    }

    public void muteNotification(String channelId, Duration duration) {
        Alert alert = alerts.get(channelId);
        if (alert != null) alert.muteNotification(duration);
    }

    public void pushNotification(String channelId, String msg) {
        Alert alert = alerts.get(channelId);
        if (alert != null) alert.pushNotification(msg);
    }
}
