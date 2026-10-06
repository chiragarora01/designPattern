package org.practice.observer3;

import org.practice.observer3.strategy.EmailNotification;
import org.practice.observer3.strategy.MultipleNotificationComposit;
import org.practice.observer3.strategy.NotificationStrategy;
import org.practice.observer3.strategy.PushNotification;

import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Stateless -> safe to share across users.
        NotificationStrategy emailNotification = new EmailNotification();
        NotificationStrategy pushNotification = new PushNotification();
        NotificationStrategy both = new MultipleNotificationComposit(List.of(emailNotification, pushNotification));

        EventNotifier channelA = new EventNotifier("channelA");
        EventNotifier channelB = new EventNotifier("channelB");

        User user = new User("anc", "1");
        User user1 = new User("ancd", "2");
        User user2 = new User("ance", "3");

        user.subscribe(channelA, emailNotification);
        user1.subscribe(channelA, pushNotification);
        user2.subscribe(channelA, both);
        user2.subscribe(channelB, both);

        Admin admin = new Admin(channelA);
        admin.start();

        // 24h in prod; short here so the auto-unmute is observable.
        user1.muteNotification("channelA", Duration.ofSeconds(1));
        // Muting channelA leaves user2's channelB subscription untouched.
        user2.muteNotification("channelA", Duration.ofSeconds(1));

        Thread.sleep(2000);
        admin.stop();
    }
}
