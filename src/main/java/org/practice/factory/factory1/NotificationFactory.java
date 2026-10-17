package org.practice.factory.factory1;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NotificationFactory {
    Map<String, Supplier<Notification>> notificationMap = new HashMap<>();

    public void registerNotificationType(String type,Notification notification){
        notificationMap.put(type,()->notification);
    }

    public Notification getNotificationType(String type) throws IllegalAccessException {

        Supplier<Notification> notificationSupplier = notificationMap.get(type);
        if(notificationSupplier==null) throw new IllegalAccessException();
        return notificationSupplier.get();

    }

}


