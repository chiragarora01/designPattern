package org.practice.observer3;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/** One instance per channel. */
public class EventNotifier {

    private final String channelId;
    Set<User> users = new CopyOnWriteArraySet<>();

    public EventNotifier(String channelId) {
        this.channelId = channelId;
    }

    public String channelId() {
        return channelId;
    }

    public void addUserInNotificationList(User user) {
        users.add(user);
    }

    public void notifyOnVideoUpload(String msg) {
        users.forEach(x -> x.pushNotification(channelId, msg));
    }
}
