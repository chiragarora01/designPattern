package org.practice.observer.observer3.strategy;

import java.util.List;

public class MultipleNotificationComposit implements NotificationStrategy{

    List<NotificationStrategy> notificationStrategies;

    public MultipleNotificationComposit(List<NotificationStrategy> notificationStrategies) {
        this.notificationStrategies = notificationStrategies;
    }

    @Override
    public void onNewVideoUploads(String msg) {
        if(notificationStrategies!=null){
            notificationStrategies.forEach(x->x.onNewVideoUploads(msg));
        }
    }
}
