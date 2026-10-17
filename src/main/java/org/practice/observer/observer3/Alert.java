package org.practice.observer.observer3;

import org.practice.observer.observer3.strategy.NotificationStrategy;

import java.time.Duration;
import java.time.Instant;

/**
 * One Alert per (user, channel). Holds the mute state, so it must never be
 * shared between users - only the stateless NotificationStrategy is shareable.
 */
public class Alert {
    private final String channelId;
    private final NotificationStrategy notificationStrategy;

    // Absolute deadline, not a boolean: null = not muted.
    // Set only by mute(), never touched by the delivery path.
    private volatile Instant muteUntil;

    public Alert(String channelId, NotificationStrategy notificationStrategy) {
        this.channelId = channelId;
        this.notificationStrategy = notificationStrategy;
    }

    public String channelId() {
        return channelId;
    }

    public void muteNotification(Duration duration) {
        this.muteUntil = Instant.now().plus(duration);
    }

    public void unmuteNotification() {
        this.muteUntil = null;
    }

    // Lazy expiry: no timer to unmute, the deadline comparison does it.
    private boolean isMuted() {
        return muteUntil != null && Instant.now().isBefore(muteUntil);
    }

    public void pushNotification(String msg) {
        if (isMuted()) return;
        notificationStrategy.onNewVideoUploads(msg);
    }
}
